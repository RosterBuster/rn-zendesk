package aero.rbgroup.rnzendesk;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;

import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;

import zendesk.configurations.Configuration;
import zendesk.core.Zendesk;
import zendesk.core.Identity;
import zendesk.core.AnonymousIdentity;
import zendesk.support.Support;
import zendesk.support.guide.ArticleConfiguration;
import zendesk.support.guide.HelpCenterActivity;
import zendesk.support.guide.HelpCenterConfiguration;
import zendesk.support.guide.ViewArticleActivity;
import zendesk.support.request.RequestActivity;
import zendesk.support.requestlist.RequestListActivity;


public class RNZendeskBridge extends ReactContextBaseJavaModule {

    public RNZendeskBridge(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        // module name
        return "RNZendesk";
    }

    // Initialization Methods

    @ReactMethod
    public void initialize(ReadableMap config) {
        String appId = config.getString("appId");
        String zendeskUrl = config.getString("zendeskUrl");
        String clientId = config.getString("clientId");
        Zendesk.INSTANCE.init(getReactApplicationContext(), zendeskUrl, appId, clientId);
        Support.INSTANCE.init(Zendesk.INSTANCE);
    }

    // Indentification Methods

    @ReactMethod
    public void identifyAnonymous(String name, String email) {
        Identity identity = new AnonymousIdentity.Builder()
                .withNameIdentifier(name)
                .withEmailIdentifier(email)
                .build();

        Zendesk.INSTANCE.setIdentity(identity);
    }

    // UI Methods

    @ReactMethod
    public void showHelpCenter(ReadableMap options) {
        ArrayList tags = options.hasKey("tags") ? options.getArray("tags").toArrayList() : new ArrayList();
        String subject = options.hasKey("subject") ? options.getString("subject") : "";
        boolean enableContactUs = !(options.hasKey("hideContactSupport") && options.getBoolean("hideContactSupport"));

        Configuration requestActivityConfig = RequestActivity.builder()
                .withRequestSubject(subject)
                .withTags(tags)
                .config();

        HelpCenterConfiguration.Builder builder = HelpCenterActivity.builder()
                .withContactUsButtonVisible(enableContactUs);

        int groupType = options.hasKey("groupType") ? options.getInt("groupType") : 0;
        ArrayList<Double> groupIds = options.hasKey("groupIds") ? getGroupIds(options) : new ArrayList();
        ArrayList<Long> longGroupIds = new ArrayList();

        for (int i = 0; i < groupIds.size(); i++) {
            longGroupIds.add(groupIds.get(i).longValue());
        }

        switch(groupType) {
            case 1: {
                builder.withArticlesForSectionIds(longGroupIds);
                break;
            }
            case 2: {
                builder.withArticlesForCategoryIds(longGroupIds);
                break;
            }
        }

        Intent hcaIntent = builder.intent(getReactApplicationContext(), requestActivityConfig);
        hcaIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getReactApplicationContext().startActivity(hcaIntent);

    }

    /**
     * Parse group ids from react native arguments.
     *
     * @param options
     * @return list of ids
     */
    private ArrayList<Double> getGroupIds(@Nullable ReadableMap options) {
        if (options == null || !options.hasKey("groupIds")) return new ArrayList<>();
        ArrayList<Double> idList = new ArrayList<>();
        for (Object groupId : options.getArray("groupIds").toArrayList()) {
            idList.add(Double.parseDouble(String.valueOf(groupId)));
        }
        return idList;
    }
}
