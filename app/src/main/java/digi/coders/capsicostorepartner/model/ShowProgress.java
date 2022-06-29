package digi.coders.capsicostorepartner.model;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import com.airbnb.lottie.LottieAnimationView;

import digi.coders.capsicostorepartner.R;

public class ShowProgress {
    
    private static Context ctx;
    private AlertDialog.Builder alert;
    private AlertDialog alertDialog;
    private static ShowProgress showProgress=new ShowProgress();

    public static ProgressDisplay progressDisplay;
    private ShowProgress() {
    }
    public static ShowProgress getShowProgress(Context context)
    {
        ctx=context;
//        progressDisplay=new ProgressDisplay(ctx);
        return showProgress;
    }

    public   void show()
    {
        alert=new AlertDialog.Builder(ctx);


//        progressDisplay.showProgress();
//
//        View view= LayoutInflater.from(ctx).inflate(R.layout.loader_layout,null);
//        alert.setView(view);
         alertDialog=alert.create();
//        alertDialog.show();
//        alertDialog.setCanceledOnTouchOutside(false);
//        alertDialog.getWindow().setLayout(600,600);
//        View view1=alertDialog.getWindow().getDecorView();
//        view1.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT))   ;

    }
    public  void   hide()
    {
//        alertDialog.hide();
//        progressDisplay.hideProgress();
    }

        public void dismiss()
        {
//            alertDialog.dismiss();
//            progressDisplay.hideProgress();
        }
}
