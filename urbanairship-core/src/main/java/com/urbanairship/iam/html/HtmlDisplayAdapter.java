/* Copyright Urban Airship and Contributors */

package com.urbanairship.iam.html;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.urbanairship.Logger;
import com.urbanairship.UAirship;
import com.urbanairship.iam.DisplayHandler;
import com.urbanairship.iam.InAppMessage;
import com.urbanairship.iam.InAppMessageAdapter;
import com.urbanairship.iam.InAppMessageManager;
import com.urbanairship.iam.assets.Assets;
import com.urbanairship.js.Whitelist;
import com.urbanairship.util.Network;

/**
 * Html display adapter.
 */
public class HtmlDisplayAdapter implements InAppMessageAdapter {

    private final InAppMessage message;

    /**
     * Default constructor.
     *
     * @param message The HTML in-app message.
     */
    protected HtmlDisplayAdapter(@NonNull InAppMessage message) {
        this.message = message;
    }

    /**
     * Creates a new modal adapter.
     *
     * @param message The in-app message.
     * @return The modal adapter.
     */
    @NonNull
    public static HtmlDisplayAdapter newAdapter(@NonNull InAppMessage message) {
        HtmlDisplayContent displayContent = message.getDisplayContent();
        if (displayContent == null) {
            throw new IllegalArgumentException("Invalid message for adapter: " + message);
        }

        return new HtmlDisplayAdapter(message);
    }

    @PrepareResult
    @Override
    public int onPrepare(@NonNull Context context, @NonNull Assets assets) {
        HtmlDisplayContent displayContent = message.getDisplayContent();
        if (displayContent == null || !UAirship.shared().getWhitelist().isWhitelisted(displayContent.getUrl(), Whitelist.SCOPE_OPEN_URL)) {
            Logger.error("HTML in-app message URL is not whitelisted. Unable to display message.");
            return InAppMessageAdapter.CANCEL;
        }

        return InAppMessageAdapter.OK;
    }

    @Override
    public boolean isReady(@NonNull Context context) {
        return Network.isConnected();
    }

    @Override
    public void onDisplay(@NonNull Context context, @NonNull DisplayHandler displayHandler) {
        Intent intent = new Intent(context, HtmlActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .putExtra(HtmlActivity.DISPLAY_HANDLER_EXTRA_KEY, displayHandler)
                .putExtra(HtmlActivity.IN_APP_MESSAGE_KEY, message);

        context.startActivity(intent);
    }

    @Override
    public void onFinish(@NonNull Context context) {}
}
