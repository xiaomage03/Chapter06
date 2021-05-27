package com.dodola.atrace;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

//权限检查
public class PermissionsChecker {
	private final Context mContext;

	public PermissionsChecker(Context context) {
		mContext = context.getApplicationContext();
	}

	public boolean isPermissions(String... params) {
		for (String permission : params) {
			if (checkPermission(permission)) {
				return true;
			}
		}
		return false;
	}

	private boolean checkPermission(String permission) {
		return ContextCompat.checkSelfPermission(mContext, permission) == PackageManager.PERMISSION_DENIED;
	}

}
