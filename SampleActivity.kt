import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.usabilla.sdk.ubform.UbConstants.INTENT_CLOSE_CAMPAIGN
import com.usabilla.sdk.ubform.UbConstants.INTENT_CLOSE_FORM
import com.usabilla.sdk.ubform.UbConstants.INTENT_ENTRIES
import com.usabilla.sdk.ubform.Usabilla
import com.usabilla.sdk.ubform.UsabillaFormCallback
import com.usabilla.sdk.ubform.UsabillaReadyCallback
import com.usabilla.sdk.ubform.sdk.entity.FeedbackResult
import com.usabilla.sdk.ubform.sdk.form.FormClient
import com.usabilla.sdk.ubform.sdk.form.model.UbFonts
import com.usabilla.sdk.ubform.sdk.form.model.UbImages
import com.usabilla.sdk.ubform.sdk.form.model.UsabillaTheme

class SampleActivity : AppCompatActivity(), UsabillaFormCallback, UsabillaReadyCallback {

    private val filter: IntentFilter = IntentFilter().also {
        it.addAction(INTENT_CLOSE_FORM)
        it.addAction(INTENT_CLOSE_CAMPAIGN)
        it.addAction(INTENT_ENTRIES)
    }
    private val fragmentTag = "MyFragment"
    private val usabilla: Usabilla = Usabilla

    private val usabillaReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                INTENT_CLOSE_FORM -> {
                    // Remove fragment from the screen
                    formClient?.let {
                        supportFragmentManager.beginTransaction().remove(it.fragment).commit()
                    }
                    // Log form data
                    intent.getParcelableExtra<FeedbackResult>(FeedbackResult.INTENT_FEEDBACK_RESULT)?.let {
                        Log.i("Form data", "Rating: ${it.rating}, Abandoned page: ${it.abandonedPageIndex}, Is sent: ${it.isSent}")
                    }
                }
                INTENT_CLOSE_CAMPAIGN -> {
                    // Log form data
                    intent.getParcelableExtra<FeedbackResult>(FeedbackResult.INTENT_FEEDBACK_RESULT_CAMPAIGN)?.let {
                        Log.i("Form data", "Rating: ${it.rating}, Abandoned page: ${it.abandonedPageIndex}, Is sent: ${it.isSent}")
                    }
                }
                INTENT_ENTRIES -> {
                    // Log form components data
                    intent.getStringExtra(FeedbackResult.INTENT_ENTRIES)?.let { formData ->
                        Log.i("Form components data", formData)
                    }
                }
            }
        }
    }

    private var formClient: FormClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeSdk()
        setUsabillaTheme() //Optional
        if (supportFragmentManager.findFragmentByTag(fragmentTag) != null) {
            formClient = supportFragmentManager.findFragmentByTag(fragmentTag) as FormClient?
            attachFragment()
        }
    }

    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(this).registerReceiver(usabillaReceiver, filter)
    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(usabillaReceiver)
    }

    override fun formLoadSuccess(form: FormClient) {
        formClient = form
        attachFragment()
    }

    override fun formLoadFail() {
        Toast.makeText(this, "Form load fail", Toast.LENGTH_LONG).show()
    }

    override fun mainButtonTextUpdated(text: String) {
        // Use this text for your own navigation button.
        // Usually returns "Next" or "Submit".
    }

    override fun onUsabillaInitialized() {
        // This callback will be called once the initialization process of the SDK finishes.
        // In case an appId was provided during initialization, then the SDK starts to be able
        // to process events right after this callback is called.
    }

    private fun attachFragment() {
        supportFragmentManager.beginTransaction()
            .replace("use frame layout here", formClient!!.fragment, fragmentTag).commit()
    }

    private fun initializeSdk() {
        // In the initialize method the third parameter defines a custom http client that can replace
        // the default one used by the SDK (Volley).
        // If `null` is passed then the default client will be used.
        usabilla.initialize(this, "use your personal AppId here", null, this)
        usabilla.debugEnabled = true
        usabilla.updateFragmentManager(supportFragmentManager)
    }

    private fun giveFeedback() {
        // Optional screenshot
        val screenshot = usabilla.takeScreenshot(this)

        // Optional theme specific for that passive form
        val newFonts = UbFonts(R.font.indie_flower)
        val theme = UsabillaTheme(newFonts, null)
        usabilla.loadFeedbackForm("use your personal FormId here", screenshot, theme, this)
    }

    private fun sendEvent() {
        usabilla.sendEvent(applicationContext, "MyEvent")
    }

    private fun resetCampaign() {
        // Reset campaign progression deleting them from memory.
        // It also fetches the campaigns associated to the appId once again
        // and calls the initialisation callback once the process is finished.
        usabilla.resetCampaignData(this, this)
    }

    private fun resetPassiveFeedbackForms() {
        // Deletes preloaded passive feedback forms from memory.
        usabilla.removeCachedForms()
    }

    private fun setUsabillaTheme() {
        // Create images
        val newImages = UbImages(
            listOf(
                R.drawable.mood_1_bw,
                R.drawable.mood_2_bw,
                R.drawable.mood_3_bw,
                R.drawable.mood_4_bw,
                R.drawable.mood_5_bw
            ),
            emptyList(),
            R.drawable.ic_star_yellow,
            R.drawable.ic_star_red
        )
        // Set the theme
        usabilla.theme = UsabillaTheme(null, newImages)
    }
}
