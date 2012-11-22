package com.example.recordvoice;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

//ロック画面
public class Lock extends Activity{
	
	Button button;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.lock);
        windowSizes();
        //getResourceSize(R.drawable.rockber_right);
        getResourceSize(R.id.imageView1);
        
        // 画面のロックを防ぐ
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
	}

	//ロック解除ボタンでCallクラスへ移動
	public void kaijo(View v){
		Intent intent = new Intent(this, Call.class);
		this.startActivity(intent);
		//this.finish();	//このアクティビティを消滅する
	}
	
	//録音再生テストボタンでRecordVoiceクラスへ移動
	public void test(View v){
		Intent intent = new Intent(this, RecordVoice.class);
		this.startActivity(intent);
		//this.finish();	//このアクティビティを消滅する
	}
	
	
	//隠しボタン（設定画面）
	public void setting(View v){
		Intent intent = new Intent("android.settings.SETTINGS");
		startActivity(intent);
	}
	
	//メニューから設定画面へ（もしものために実装）
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.getMenuInflater().inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.item1:
			Intent intent = new Intent("android.settings.SETTINGS");
			startActivity(intent);
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	//アプリを終了させる
	//異常終了
	//フロントカメラ
	
	//画面のサイズを取得してlogに出す
	public void windowSizes() {
		WindowManager wm = getWindowManager();
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
 
        Log.d("DISPLAY", "density：" + metrics.density);
        Log.d("DISPLAY", "densityDpi：" + metrics.densityDpi);
        Log.d("DISPLAY", "scaledDensity：" + metrics.scaledDensity);
        Log.d("DISPLAY", "widthPixels：" + metrics.widthPixels);
        Log.d("DISPLAY", "heightPixels：" + metrics.heightPixels);
        Log.d("DISPLAY", "xdpi：" + metrics.xdpi);
        Log.d("DISPLAY", "ydpi：" + metrics.ydpi);

	}

	
	//リソースの画像サイズを取得する
	public void getResourceSize(int resourceName) {
		//リソースからbitmapを作成
		/*Bitmap rockImage = BitmapFactory.decodeResource(this.getResources(), resourceName);
		Log.d("rockberのサイズ", "rockImage.getWidth()-->" + rockImage.getWidth() + "rockImage.getHeight()-->" + rockImage.getHeight());*/
		ImageView image = (ImageView)findViewById(resourceName);
		Log.d("image.getTop() : image.getRight()",String.valueOf(image.getTop()) + " : " + String.valueOf(image.getLeft()));
		image.layout(image.getRight() + 200, image.getTop(), (image.getRight() + 200) + image.getWidth(), (image.getTop()) + image.getHeight());
	}
	
}
