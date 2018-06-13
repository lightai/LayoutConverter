package com.netease.layoutconverter;

import android.content.Context;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.util.Pools;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Space;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * View预创建策略
 *
 * Created by hzwangpeng2015 on 2018/2/8.
 */
public class ViewFactory {
    private static final String TAG = "ViewFactory";

    static final Map<Class, Pools.Pool<View>> viewCache = new HashMap<>(50);
    private static List<ViewCreator> viewCreatorList = new ArrayList<>();

    /**
     * fixme:某些widget并发创建会出现低概率崩溃
     */
    static final Class<?>[] NOT_PRE_CREATE = new Class[]{Button.class, RadioButton.class,
            ToggleButton.class, CheckBox.class, Switch.class};

    private static final HashMap<Class, Constructor<? extends View>> sConstructorMap =
            new HashMap<>();

    private static final Class<?>[] mConstructorSignature = new Class[]{Context.class};

    private static final ViewFactory viewFactory = new ViewFactory();

    public static ViewFactory getInstance() {
        return viewFactory;
    }

    <T extends View> T obtainView(Context context, Class<T> cls) {
        T view = null;

        final Pools.Pool<View> pool = viewCache.get(cls);
        if (pool != null) {
            view = (T) pool.acquire();
            if (view != null) {
                Context viewContext = view.getContext();
                if (viewContext instanceof ViewContext) {
                    ((ViewContext) viewContext).setCurrentContext(context);
                }
            }
        }

        if (view == null) {
            view = createView(cls, new ViewContext(context));
        }

        preCreateView(context, cls);

        return view;
    }

    private void preCreateView(Context context, Class cls) {
        for (Class c : NOT_PRE_CREATE) {
            if (c == cls) {
                return;
            }
        }
        CreateViewThread.getInstance().enqueue(new ViewFactory.CreateViewWorker(context, cls, 1));
    }

    <T extends View> T createView(Class<T> cls, Context viewContext) {
        if (cls == TextView.class) {
            return (T) new TextView(viewContext);
        } else if (cls == Button.class) {
            return (T) new Button(viewContext);
        } else if (cls == ListView.class) {
            return (T) new ListView(viewContext);
        } else if (cls == ImageView.class) {
            return (T) new ImageView(viewContext);
        } else if (cls == ImageButton.class) {
            return (T) new ImageButton(viewContext);
        } else if (cls == View.class) {
            return (T) new View(viewContext);
        } else if (cls == Space.class) {
            return (T) new Space(viewContext);
        } else if (cls == ProgressBar.class) {
            return (T) new ProgressBar(viewContext);
        } else if (cls == ImageButton.class) {
            return (T) new ImageButton(viewContext);
        } else if (cls == CheckBox.class) {
            return (T) new CheckBox(viewContext);
        } else if (cls == RadioButton.class) {
            return (T) new RadioButton(viewContext);
        } else if (cls == RadioGroup.class) {
            return (T) new RadioGroup(viewContext);
        } else if (cls == ToggleButton.class) {
            return (T) new ToggleButton(viewContext);
        }

        T view;
        for (ViewCreator viewCreator: viewCreatorList) {
            view = viewCreator.onCreateView(cls, viewContext);
            if (view != null) {
                return view;
            }
        }

        return defaultCreateView(cls, viewContext);
    }

    public  <T extends View> T defaultCreateView(Class<T> clazz, Context context) {
        Constructor<? extends View> constructor = sConstructorMap.get(clazz);

        try {
            if (constructor == null) {
                constructor = clazz.getConstructor(mConstructorSignature);
                constructor.setAccessible(true);
                sConstructorMap.put(clazz, constructor);
            }

            final View view = constructor.newInstance(context);
            return (T) view;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void addViewCreator(ViewCreator viewCreator) {
        if(!viewCreatorList.contains(viewCreator)) viewCreatorList.add(viewCreator);
    }

    public static void earlyCreateView(Context context, @NonNull List<Class> views, int count) {
        for (Class cls : views) {
            CreateViewThread.getInstance().enqueue(new CreateViewWorker(context, cls, count));
        }
    }

    /**
     * 清空view对象池
     */
    public static void clearCache() {
        viewCache.clear();
    }

    public interface ViewCreator {
        <T extends View> T onCreateView(Class<T> cls, Context viewContext);
    }

    final static class CreateViewWorker implements Runnable {
        final Class viewClass;
        final int createCount;
        final Context context;

        CreateViewWorker(Context context, Class viewClass, int createCount) {
            this.viewClass = viewClass;
            this.createCount = createCount;
            this.context = context;
        }

        @Override
        public void run() {
            Pools.Pool<View> pool = viewCache.get(viewClass);
            if (pool == null) {
                pool = new Pools.SynchronizedPool<>(50);
                viewCache.put(viewClass, pool);
            }
            for (int i = 0; i < createCount; i++) {
                try {
                    final View view = ViewFactory.getInstance().createView(
                            viewClass, new ViewContext(context));
                    if (view != null) {
                        pool.release(view);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public String toString() {
            return "CreateViewWorker{" +
                    "viewClass=" + viewClass +
                    ", createCount=" + createCount +
                    '}';
        }
    }

    /**
     * 预创建View对象的后台线程
     */
    public static class CreateViewThread extends Thread {
        private static final CreateViewThread sInstance;
        static {
            sInstance = new CreateViewThread();
            sInstance.setName("LayoutConverter Thread");
            sInstance.start();
        }

        static CreateViewThread getInstance() {
            return sInstance;
        }

        private LinkedBlockingQueue<CreateViewWorker> mQueue = new LinkedBlockingQueue<>(500);

        // Extracted to its own method to ensure locals have a constrained liveness
        // scope by the GC. This is needed to avoid keeping previous request references
        // alive for an indeterminate amount of time, see b/33158143 for details
        void runInner() {
            CreateViewWorker request;
            try {
                request = mQueue.take();
            } catch (InterruptedException ex) {
                // Odd, just continue
                Log.w(TAG, ex);
                return;
            }

            try {
                request.run();
            } catch (RuntimeException ex) {
                // Probably a Looper failure, retry on the UI thread
                Log.w(TAG, "Failed to inflate resource in the background! Retrying on the UI"
                        + " thread", ex);
            }
        }

        @Override
        public void run() {
            ThreadLocal<Looper> sThreadLocal = null;
            try {
                Field field = Looper.class.getDeclaredField("sThreadLocal");
                field.setAccessible(true);
                Object object = field.get(Looper.getMainLooper());
                if (object instanceof ThreadLocal) {
                    sThreadLocal = (ThreadLocal<Looper>) object;
                    sThreadLocal.set(Looper.getMainLooper());
                }
                Log.i("AsyncLayoutInflater", "myLooper:" + Looper.myLooper());
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            while (true) {
                runInner();
            }
        }

        void enqueue(CreateViewWorker request) {
            if (!mQueue.offer(request)) {
                Log.w(TAG, String.format("enqueue %s request failed", request));
            }
        }
    }
}
