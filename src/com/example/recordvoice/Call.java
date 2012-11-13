package com.example.recordvoice;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Size;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

//発信中
public class Call extends Activity implements Camera.PictureCallback,OnClickListener{
	
	Timer timer;
	int counter;
	int limit = 5;	//次の画面へ移動するまでの秒
	TextView tv2;
	MediaPlayer mp;
	int numberOfCameras; 
	Button button;	//写真を撮るボタン
	public Camera camera;	//カメラオブジェクト
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.call);
        tv2 = (TextView)this.findViewById(R.id.textView2);
        
        //5秒経過でInCallアクティビティへ
        this.startTimer();
        
        //コール音の再生
        this.call();
        
        //終了ボタン（写真を撮る）
        button = (Button)this.findViewById(R.id.button1);
        button.setOnClickListener(this);
 		
 		// 利用可能なカメラの個数を取得
 	    numberOfCameras = Camera.getNumberOfCameras();
 	    System.out.println(numberOfCameras);
        //カメラをオープン
 		camera = Camera.open(cameraDevicesId());
 		//プレビュー開始
 		camera.startPreview();
	}
	
	//コール音の再生
	public void call() {
		// リソースID指定
		mp = MediaPlayer.create(this,R.raw.call);
		mp.setLooping(true);//ループ再生
		mp.seekTo(0);		//再生位置0ミリ秒
		mp.start();			//再生開始
    }
	public Integer cameraDevicesId() {
		Integer defaultCameraId = 0;
		CameraInfo cameraInfo = new CameraInfo();
		for (int i = 0; i < numberOfCameras; i++) {
			Camera.getCameraInfo(i, cameraInfo);
			if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {
				defaultCameraId = i;
			}
		}
		Log.d("cameraDevicesNumber()",defaultCameraId.toString());
		return defaultCameraId;
	}
	
	//5秒タイマー
	public void startTimer(){
		if(timer != null) timer.cancel();
		timer = new Timer();
		final android.os.Handler handler = new android.os.Handler();
		timer.schedule(new TimerTask(){
			@Override
			public void run() {
				handler.post(new Runnable(){
					public void run(){
						if(counter == limit){
							timer.cancel();
							next();
							//if(wl.isHeld()) wl.release();
							//showAlarm();
						}else{
							//CountdownTimerActivity.countdown(counter);
							counter = counter+1;
							showCount();
						}
					}
				});
			}
		},0,1000);
	}
	
	//カウントの表示
	public void showCount(){
		tv2.setText(String.valueOf(counter));
	}
	
	//5秒経過でInCallアクティビティへ
	public void next(){
		mp.stop();	//再生停止
		Intent intent = new Intent(this, InCall.class);
		this.startActivity(intent);
		this.finish();	//このアクティビティを消滅する
	}
	
    
	//終了ボタンを押したとき(写真を撮る)
	public void onClick(View v){
		/* 画像を保存するときの画像サイズを変更 */ 
		//カメラパラメータオブジェクトの取得
		Camera.Parameters param = camera.getParameters();
		//カメラのサイズを入れるインスタンス
		Size pictureSize;
		//端末がサポートするpictureサイズを取得する
		List<Size> supportedPictureSizes = param.getSupportedPictureSizes();
		int i = 0;
		for (Size size : supportedPictureSizes) {
			System.out.println("縦の長さ：" + supportedPictureSizes.get(i).height + " 横の長さ：" + supportedPictureSizes.get(i).width);
			i++;
		}
		//縦の長さ480*横の長さ640
		pictureSize = supportedPictureSizes.get(0);
		//paramにサイズをセットする
		param.setPictureSize(pictureSize.width, pictureSize.height);
		//cameraにサイズをsetする
		camera.setParameters(param);
		/* 画像サイズ変更終了 */
		//写真を撮った後、自動的にonPictureTaken()を呼び出す
		camera.takePicture(null, null, null, this);
	}
	@Override
	//写真を撮った後、自動的に呼ばれる
	public void onPictureTaken(byte[] data, Camera c) {
		try{
			// SDカードのディレクトリ
	        File dir = Environment.getExternalStorageDirectory();
	        // アプリ名で
	        File appDir = new File(dir, "RecordVoice");
	        // ディレクトリを作る
	        if (!appDir.exists()) appDir.mkdir();
	        // ファイル名（現在時刻.jpg)
//	        String name = System.currentTimeMillis() + ".jpg";
	        String name = "testImage.jpg";
	        // 出力ファイルのパス
	        String path = new File(appDir, name).getAbsolutePath();
	        //String path = Environment.getExternalStorageDirectory()+ "/Android/" + System.currentTimeMillis()+ ".jpg";
	        
	        //写真データをファイルに書き込み
			data2file(data,path);
			
			//ギャラリーに登録（APIレベル8）
			//第1引数:context,第2引数:path配列,第3引数:MimeType配列,第4引数:OnScanCompletedリスナー
			String[] paths = {path};
			String[] mimeTypes = {"image/jpeg"};
			MediaScannerConnection.scanFile(this, paths, mimeTypes, sc);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		//プレビュー再開
		camera.startPreview();
	}
	
	//写真データをファイルに書き込み
    private void data2file(byte[] data,String fileName) throws Exception {
        FileOutputStream out=null;
        try {
            out=new FileOutputStream(fileName);
            out.write(data);
            out.close();
        } catch (Exception e) {
            if (out!=null) {
            	out.close();
            }
            throw e;
        }
    }
    
	//ギャラリーに登録したあと呼ばれる
    OnScanCompletedListener sc = new OnScanCompletedListener() {
		@Override
		public void onScanCompleted(String path, Uri uri) {
			
		}
	};
    
    //アクティビティ終了時
	@Override
	protected void onStop() {
		super.onStop();
		// プレビューを停止
        camera.stopPreview();
        // カメラをリリース
        camera.release();
        camera=null;
	}
	
	//戻るボタン無効
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
	    // TODO Auto-generated method stub
	    if (event.getAction()==KeyEvent.ACTION_DOWN) {
	    	//if(event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
	    	if(event.getKeyCode() == KeyEvent.KEYCODE_HOME) {
	            return false;
	        }
	    }
	    return super.dispatchKeyEvent(event);
	}
}
