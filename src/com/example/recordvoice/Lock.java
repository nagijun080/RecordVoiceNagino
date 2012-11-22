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
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

//ロック画面
public class Lock extends Activity implements OnTouchListener{
	
	Button button;
	int currentX = 0;
	int currentY = 0;
	int offsetX = 0; //画面タッチ位置の座標 : X軸
	int offsetY = 0;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.lock);
        //rock_leftをonTouchリスナーに登録
        View image = findViewById(R.id.imageView1);
        image.setOnTouchListener(this);
        image.getLeft();
        image.getTop();
        image.layout(Integer.valueOf(-310), Integer.valueOf(0), Integer.valueOf(-310) + image.getWidth(), image.getHeight());
        //windowSizes();
        //getResourceSize(R.drawable.rockber_right);
        //getResourceSize(R.id.imageView1);
        
        // 画面のロックを防ぐ
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
	}

	//ロック解除ボタンでCallクラスへ移動
	public void kaijo(){
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

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO 自動生成されたメソッド・スタブ
		int x = (int)event.getRawX();
		int y = (int)event.getRawY();
		Log.d("onTouch", "x:" + x + " y:" + y);
		if (v == findViewById(R.id.imageView1)) {
			if (x >= 550) {
				kaijo();
			}
		}
		switch(event.getAction()) {
		
		case MotionEvent.ACTION_DOWN:
			currentX = v.getLeft();
			currentY = v.getTop();
			offsetX = x;
			offsetY = y;
			Log.d("ACTION_DOWN", "currentX:" + currentX +
					" currentY:" + currentY + " offsetX:" + offsetX + " offsetY:" + offsetY);
			break;
		case MotionEvent.ACTION_MOVE:
			int diffX = offsetX - x;
			int diffY = offsetY - y;
			currentX -= diffX;
			currentY -= diffY;
			Log.d("Action_move", "diffX:" + diffX + " diffY:" + diffY + " currentX:" + currentX + " currentY:" + currentY);
			//画像の移動
			v.layout(currentX, currentY, currentX + v.getWidth(), currentY + v.getHeight());
			offsetX = x;
			offsetY = y;
			Log.d("Action_move", "offsetX:" + offsetX + " offsetY:" + offsetY + " currentX:" + currentX + " currentY:" + currentY);
			break;
		case MotionEvent.ACTION_UP:
			v.layout(0, 0, v.getWidth(), v.getHeight());
			break;
		
		}
		return true;
	}
	
	//アプリを終了させる
	//異常終了
	//フロントカメラ
	
}
