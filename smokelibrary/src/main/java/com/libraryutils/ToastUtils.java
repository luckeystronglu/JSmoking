package com.libraryutils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils
{
  private static Toast toast = null;

  public static void showTextToast(Context context, String msg) {
    if (toast == null)
      toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
    else {
      toast.setText(msg);
    }
    toast.show();
  }

  public static void showTextLongToast(Context context, String msg) {
    if (toast == null)
      toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
    else {
      toast.setText(msg);
    }
    toast.show();
  }

  public static void showTextToast(Context context, String msg, int showTimeLong)
  {
    if (toast == null)
      toast = Toast.makeText(context, msg, showTimeLong);
    else {
      toast.setText(msg);
    }
    toast.show();
  }
}