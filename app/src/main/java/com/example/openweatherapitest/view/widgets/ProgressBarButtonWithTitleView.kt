package com.example.openweatherapitest.view.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.example.openweatherapitest.R
import com.example.openweatherapitest.databinding.ViewProgressBarButtonBinding
import kotlin.math.max
import kotlin.math.min



class ProgressBarButtonWithTitleView(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val TAG = ProgressBarButtonWithTitleView::class.java.simpleName

    enum class TitleDisplayMode { // order must match enum declaration from attrs!!!
        NORMAL,
        EMPHASIS,
        GONE,
    }

    enum class ProgressBarButtonDisplayMode { // order must match enum declaration from attrs!!!
        INDETERMINATE_MODE,
        BUTTON_MODE,
        PROGRESS_MODE,
    }

    private var progressButtonDisplayMode: ProgressBarButtonDisplayMode? = null
    private var titleDisplayMode: TitleDisplayMode? = null

    private var title: String? = null
    private var buttonLabel: String? = null
    private var progressValue: Int = 0

    private val bindings: ViewProgressBarButtonBinding

    constructor(context: Context) : this(context, null, 0, 0)
    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0, 0)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)

    init {
        this.bindings = ViewProgressBarButtonBinding.inflate(LayoutInflater.from(context), this, true)

        if (attrs != null) {
            Log.d(TAG, "got attrs")
            try {
                val ta = context.obtainStyledAttributes(attrs, R.styleable.ProgressBarButtonWithTitleView)
                this.progressButtonDisplayMode = ProgressBarButtonDisplayMode.entries.getOrNull(ta.getInt(R.styleable.ProgressBarButtonWithTitleView_ProgressBarButtonDisplayMode, 0))
                this.titleDisplayMode = TitleDisplayMode.entries.getOrNull(ta.getInt(R.styleable.ProgressBarButtonWithTitleView_InfoTitleDisplayMode, 0))
                this.title = ta.getString(R.styleable.ProgressBarButtonWithTitleView_Title)
                this.buttonLabel = ta.getString(R.styleable.ProgressBarButtonWithTitleView_ButtonLabel)
                this.progressValue = min(100, max(0, ta.getInt(R.styleable.ProgressBarButtonWithTitleView_progressValue, 0)))
                ta.recycle()
            } catch (e: Exception) {
                this.progressButtonDisplayMode = ProgressBarButtonDisplayMode.INDETERMINATE_MODE
                this.titleDisplayMode = TitleDisplayMode.NORMAL
                this.title = null
                this.buttonLabel = null
                this.progressValue = 0
            }
        } else {
            Log.d(TAG, "did not get attrs")
            this.progressButtonDisplayMode = ProgressBarButtonDisplayMode.INDETERMINATE_MODE
            this.titleDisplayMode = TitleDisplayMode.NORMAL
            this.title = null
            this.buttonLabel = null
            this.progressValue = 0
        }


        updateProgressBarButtonDisplayMode(this.progressButtonDisplayMode)
        updateTitleDisplayMode(this.titleDisplayMode)
        Log.d(TAG, "{progressButtonDisplayMode=$progressButtonDisplayMode; titleDisplayMode=$titleDisplayMode, title=$title progress=$progressValue}")
    }

    private fun updateTitleDisplayMode(titleDisplayMode: TitleDisplayMode?) {
        this.titleDisplayMode = titleDisplayMode
        when (this.titleDisplayMode) {
            TitleDisplayMode.NORMAL -> {
                this.bindings.infoTitle.visibility = VISIBLE
                this.bindings.infoTitle.setTypeface(this.bindings.infoTitle.typeface, Typeface.NORMAL)
                this.bindings.infoTitle.setTextColor(ContextCompat.getColor(context, R.color.black))
                this.bindings.infoTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.progress_title_normal))
                this.bindings.infoTitle.text = this.title
            }
            TitleDisplayMode.EMPHASIS -> {
                this.bindings.infoTitle.visibility = VISIBLE
                this.bindings.infoTitle.setTypeface(this.bindings.infoTitle.typeface, Typeface.BOLD)
                this.bindings.infoTitle.setTextColor(ContextCompat.getColor(context, R.color.purple_dark))
                this.bindings.infoTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.progress_title_big))
                this.bindings.infoTitle.text = this.title
            }
            TitleDisplayMode.GONE -> {
                this.bindings.infoTitle.visibility = GONE
                this.bindings.infoTitle.setTypeface(this.bindings.infoTitle.typeface, Typeface.BOLD)
                this.bindings.infoTitle.setTextColor(ContextCompat.getColor(context, R.color.purple_dark))
                this.bindings.infoTitle.text = null
            }
            else -> {
                this.bindings.infoTitle.visibility = VISIBLE
                this.bindings.infoTitle.setTypeface(this.bindings.infoTitle.typeface, Typeface.BOLD)
                this.bindings.infoTitle.setTextColor(ContextCompat.getColor(context, R.color.red))
                this.bindings.infoTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.progress_title_big))
                this.bindings.infoTitle.text = "Unsupported TitleDisplayMode attribute !!!"
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateProgressBarButtonDisplayMode(progressBarButtonDisplayMode: ProgressBarButtonDisplayMode?) {
        this.progressButtonDisplayMode = progressBarButtonDisplayMode
        when (this.progressButtonDisplayMode) {
            ProgressBarButtonDisplayMode.INDETERMINATE_MODE -> {
                this.bindings.layoutForIndeterminateMode.visibility = VISIBLE
                this.bindings.layoutForButtonMode.visibility = GONE
                this.bindings.layoutForProgressMode.visibility = GONE
            }
            ProgressBarButtonDisplayMode.BUTTON_MODE -> {
                this.bindings.layoutForIndeterminateMode.visibility = GONE
                this.bindings.layoutForButtonMode.visibility = VISIBLE
                this.bindings.layoutForProgressMode.visibility = GONE
                this.bindings.buttonModeTitleTextView.setTextColor(ContextCompat.getColor(context, R.color.white))
                this.bindings.buttonModeTitleTextView.text = this.buttonLabel
            }
            ProgressBarButtonDisplayMode.PROGRESS_MODE -> {
                this.bindings.layoutForIndeterminateMode.visibility = GONE
                this.bindings.layoutForButtonMode.visibility = GONE
                this.bindings.layoutForProgressMode.visibility = VISIBLE

                val fractionalFill = this.progressValue / 100f
                this.bindings.progressModePercentageTextView.text = "${this.progressValue}%"
                (this.bindings.progressModeFillWeightedView.layoutParams as? LayoutParams)?.let {
                    it.weight = fractionalFill
                }
                (this.bindings.progressModeEmptySpaceWeightedView.layoutParams as? LayoutParams)?.let {
                    it.weight = 1f - fractionalFill
                }

                if (this.progressValue > 90) {
                    bindings.progressModePercentageTextView.setTextColor(ContextCompat.getColor(context, R.color.white))
                } else {
                    bindings.progressModePercentageTextView.setTextColor(ContextCompat.getColor(context, R.color.purple_dark))
                }
            }
            else -> {
                this.progressButtonDisplayMode = null
                this.bindings.layoutForIndeterminateMode.visibility = GONE
                this.bindings.layoutForButtonMode.visibility = VISIBLE
                this.bindings.layoutForProgressMode.visibility = GONE

                this.bindings.layoutForButtonMode.background = null
                this.bindings.layoutForButtonMode.setBackgroundColor(ContextCompat.getColor(context, R.color.red))
                this.bindings.buttonModeTitleTextView.setTextColor(ContextCompat.getColor(context, R.color.black))
                this.bindings.buttonModeTitleTextView.text = "Unsupported ProgressBarButtonDisplayMode attribute !!!"
            }
        }
    }

    /**
     * Set the amount of progress filled. It will not change the display mode though.
     * @param progressPercentage is expected to be in [0, 100] and clamped otherwise
     */
    @SuppressLint("SetTextI18n")
    fun setProgressMode(progressPercentage: Int) {
        this.progressValue = min(100, (max(0, progressPercentage)))
        this.bindings.mainProgressbarCardView.setOnClickListener(null)
        updateProgressBarButtonDisplayMode(ProgressBarButtonDisplayMode.PROGRESS_MODE)
    }

    fun setButtonMode(buttonLabel: String, onClickListener: OnClickListener) {
        this.buttonLabel = buttonLabel
        this.bindings.mainProgressbarCardView.setOnClickListener(onClickListener)
        updateProgressBarButtonDisplayMode(ProgressBarButtonDisplayMode.BUTTON_MODE)
    }

    fun setButtonIndeterminateMode() {
        this.bindings.mainProgressbarCardView.setOnClickListener(null)
        updateProgressBarButtonDisplayMode(ProgressBarButtonDisplayMode.INDETERMINATE_MODE)
    }

    fun setTitle(info: String, displayMode: TitleDisplayMode) {
        this.title = info
        updateTitleDisplayMode(displayMode)
    }

}