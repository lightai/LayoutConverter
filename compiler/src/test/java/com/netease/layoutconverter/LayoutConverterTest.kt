package com.netease.layoutconverter

import org.junit.Test

class LayoutConverterTest : BaseLayoutConverterTest() {
    @Test fun testDimensions() = doLayoutTest()
    @Test fun testApp() = doLayoutTest()
    @Test fun testTmp() = doLayoutTest()
    @Test fun testInvalidAttr() = doLayoutTest()

    // aosp widget test
    @Test fun testView() = doLayoutTest()
    @Test fun testTextView() = doLayoutTest()
    @Test fun testImageView() = doLayoutTest()
    @Test fun testAbsListView() = doLayoutTest()
    @Test fun testListView() = doLayoutTest()
    @Test fun testExpandableListView() = doLayoutTest()
    @Test fun testProgressBar() = doLayoutTest()

    // layout test
    @Test fun testViewGroup() = doLayoutTest()
    @Test fun testLinearLayout() = doLayoutTest()
    @Test fun testRelativeLayout() = doLayoutTest()

    @Test fun testInclude() = doLayoutTest()

    // not yet support
    @Test fun testMerge() = doLayoutTest()
}