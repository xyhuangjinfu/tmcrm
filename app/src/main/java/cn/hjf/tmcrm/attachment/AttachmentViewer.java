package cn.hjf.tmcrm.attachment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;

import java.io.File;

public class AttachmentViewer {

	public static void view(Context context, Attachment attachment) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

		File file = new File(attachment.getFilePath());
		Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);

		intent.setDataAndType(uri, attachment.getMimeType());
		context.startActivity(intent);
	}
}
