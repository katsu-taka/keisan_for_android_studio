package com.example.keisan;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private TextView textstatus, textquestion, textanswer, textcount;
	private SoundPool mSoundPool;
	private int mSoundOkID, mSoundNgID;
	int result, answer, rightcnt, total;
	// [0]:ゲームモード,[1]:正解数, [2]:出力数, [3]:回答率, [4]:ハイスコア値
	private String[] aryAnswer = new String[5];
	private NormalModeTask normaltask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// TextViewStatus取得
		textstatus = (TextView) findViewById(R.id.textViewStatus);
		// TextViewQuestion取得
		textquestion = (TextView) findViewById(R.id.textViewQuestion);
		// textViewAnswer取得
		textanswer = (TextView) findViewById(R.id.textViewAnswer);
		// textViewcounter取得
		textcount = (TextView) findViewById(R.id.textViewcounter);
		// SoundPoolの初期化
		mSoundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
		// 音声データの読み込み開始
		mSoundOkID = mSoundPool.load(this, R.raw.se_ok_btn, 1);
		mSoundNgID = mSoundPool.load(this, R.raw.se_ng_btn, 1);
		// 正解率/出題数初期表示表示
		aryAnswer[1] = "0";
		aryAnswer[2] = "0";
		textstatus.setText(aryAnswer[1] + " / " + aryAnswer[2]);

		// タスクの生成
		Intent intent = getIntent();
		String[] str = intent.getStringArrayExtra("mode");
		if (str[0].equals("normal")) {
			normaltask = new NormalModeTask(this);
			normaltask.execute();
		}

		// 計算問題を準備
		newQuestion();
	}

	@Override
	public void onPause() {
		super.onPause();
		// SoundPoolの解放
		mSoundPool.release();
	}

	public void inputNumber(View view) {
		String oldstr;
		oldstr = textanswer.getText().toString();
		switch (view.getId()) {
		case R.id.button1:
			textanswer.setText(oldstr + "1");
			break;
		case R.id.button2:
			textanswer.setText(oldstr + "2");
			break;
		case R.id.button3:
			textanswer.setText(oldstr + "3");
			break;
		case R.id.button4:
			textanswer.setText(oldstr + "4");
			break;
		case R.id.button5:
			textanswer.setText(oldstr + "5");
			break;
		case R.id.button6:
			textanswer.setText(oldstr + "6");
			break;
		case R.id.button7:
			textanswer.setText(oldstr + "7");
			break;
		case R.id.button8:
			textanswer.setText(oldstr + "8");
			break;
		case R.id.button9:
			textanswer.setText(oldstr + "9");
			break;
		case R.id.button10:
			if (oldstr != "") {
				textanswer.setText(oldstr + "0");
				break;
			}
		}
	}

	public void inputClear(View view) {
		// 入力値クリア
		textanswer.setText(null);
	}

	public void inputEnter(View view) {
		String resultstr;
		if (textanswer.getText().length() == 0) {
			return;
		}
		// 入力値取得
		answer = Integer.parseInt(textanswer.getText().toString());
		total++;
		// 正誤判定
		if (answer == result) {
			// 音声の再生
			mSoundPool.play(mSoundOkID, 1.0F, 1.0F, 0, 0, 1.0F);
			resultstr = "正解";
			rightcnt++;
		} else {
			// 音声の再生
			mSoundPool.play(mSoundNgID, 1.0F, 1.0F, 0, 0, 1.0F);
			resultstr = "不正解";
		}

		// トースト表示
		Toast.makeText(this, resultstr, Toast.LENGTH_SHORT).show();
		textanswer.setText(null);

		// 正解率/出題数表示
		aryAnswer[1] = String.valueOf(rightcnt);
		aryAnswer[2] = String.valueOf(total);
		textstatus.setText(aryAnswer[1] + " / " + aryAnswer[2]);
		// 問題再表示
		newQuestion();
	}

	private void newQuestion() {
		Random r = new Random();
		int n1 = r.nextInt(8) + 1;
		int n2 = r.nextInt(8) + 1;
		// 問題表示
		textquestion.setText(String.valueOf(n1) + " × " + String.valueOf(n2)
				+ " = ?");
		result = n1 * n2;
	}

	public class NormalModeTask extends AsyncTask<Void, Integer, Void> {
		public TextView remaintext;
		public Context context;
		public String remainText;
		long startTimeMillis, currentTimeMillis, diff;

		public NormalModeTask(Context context) {
			this.context = context;
		}

		@Override
		protected Void doInBackground(Void... params) {
			// カウントダウン
			// 開始時刻取得
			startTimeMillis = System.currentTimeMillis();
			// 現在時刻取得
			currentTimeMillis = System.currentTimeMillis();
			// 現在時刻 - 開始時刻
			diff = currentTimeMillis - startTimeMillis;

			// 現在時刻 - 開始時刻 < 1 の間継続
			while (diff < 60000) {
				try {
					// 100ミリ秒停止
					Thread.sleep(100);
				} catch (InterruptedException e) {
				}

				// 経過時間計算
				currentTimeMillis = System.currentTimeMillis();
				diff = currentTimeMillis - startTimeMillis;
				publishProgress((int) diff);
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
			// 時間表示フォーマット、カウントダウン計算
			SimpleDateFormat sdf = new SimpleDateFormat("mm:ss:SSSS");
			timeUpdate(sdf.format(60000 - progress[0]));
		}

		protected void onPostExecute(Void result) {
			gameEnd();
		}
	}

	// カウントダウン表示
	public void timeUpdate(String remainText) {
		textcount.setText(remainText);
	}

	// ゲーム終了
	public void gameEnd() {
		// 「もう一度」ボタン押下、正解率計算
		if (rightcnt != 0 && total != 0) {
			DecimalFormat df = new DecimalFormat("##0.000%");
			aryAnswer[3] = String.valueOf(df.format((double) rightcnt / total));
		} else {
			aryAnswer[3] = "0.000%";
		}

		// ゲーム画面（TitleActivity -> MainActivity）を開く
		Intent intent = new Intent();
		aryAnswer[0] = "normal";
		intent.putExtra("mode", aryAnswer);
		setResult(Activity.RESULT_OK, intent);
		finish();
	}
}
