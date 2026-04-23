package com.example.bluescan

import android.content.Context
import android.os.Build
import android.view.View
import android.widget.HorizontalScrollView

class ScrollSync(private val header: HorizontalScrollView, private val body: HorizontalScrollView) {
    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            header.setOnScrollChangeListener { _, scrollX, _, _, _ ->
                body.scrollTo(scrollX, body.scrollY)
            }
            body.setOnScrollChangeListener { _, scrollX, _, _, _ ->
                header.scrollTo(scrollX, header.scrollY)
            }
        } else {
            // Legacy support omitted for brevity, assuming MinSDK 24+ (matches gradle)
            // But waitForPreviousTools check ViewTreeObserver
        }
    }
}
