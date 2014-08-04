package com.kevinkarabon.glasstennis;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import com.google.android.glass.touchpad.GestureDetector.ScrollListener;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.MotionEvent;
import android.widget.TextView;

import com.kevinkarabon.glasstennis.Game;

import org.apache.commons.lang3.time.StopWatch;

public class TennisMainActivity extends Activity {
	
	private boolean mResumed;
	
	private GestureDetector mGestureDetector;
	
	private TextView mHomeScoreView;
	private TextView mAwayScoreView;
	private TextView mHomeGameScoreView;
	private TextView mAwayGameScoreView;
	private TextView mHomeSetScoreView;
	private TextView mAwaySetScoreView;
	private TextView mServingView;
	private TextView mHours;
	private TextView mMinutes;
	private TextView mSeconds;
	
	public int hours;
	public int minutes;
	public int seconds;
	
	private Stack<Game> mGameHistory;
	private Game mGame;
	
	private StopWatch mStartTime;
	private Timer mTimer;
	private Handler mHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tennis_main);
		mHomeScoreView = (TextView) findViewById(R.id.homeScore);
		mAwayScoreView = (TextView) findViewById(R.id.awayScore);
		mHomeGameScoreView = (TextView) findViewById(R.id.homeGameScore);
		mAwayGameScoreView = (TextView) findViewById(R.id.awayGameScore);
		mHomeSetScoreView = (TextView) findViewById(R.id.homeSetScore);
		mAwaySetScoreView = (TextView) findViewById(R.id.awaySetScore);
		mServingView = (TextView) findViewById(R.id.serving);
		mHours = (TextView) findViewById(R.id.hours);
		mMinutes = (TextView) findViewById(R.id.minutes);
		mSeconds = (TextView) findViewById(R.id.seconds);
		mGame = new Game();
		mGameHistory = new Stack<Game>();
		mGameHistory.push(mGame);
		mGestureDetector = createGestureDetector(this);
		mStartTime = new StopWatch();
		mStartTime.start();
		mHandler = new Handler();
		mHandler.removeCallbacks(mUpdateClock);
		mHandler.postDelayed(mUpdateClock, 250);
		updateServing();
	}
	
	private GestureDetector createGestureDetector(Context context) {
		GestureDetector gestureDetector = new GestureDetector(context);
		gestureDetector.setBaseListener(new GestureDetector.BaseListener() {
			@Override
			public boolean onGesture(Gesture gesture) {
				
				if (gesture == Gesture.TAP){
					if (!mGameHistory.isEmpty()) {
						Game tempGame = mGameHistory.peek();
						mGame.equalsGame(tempGame);
						mGameHistory.pop();
						updateServing();
						updateView();
					}
					return true;
				}
				
				else if (gesture == Gesture.SWIPE_RIGHT){
					Game tempGame = new Game();
					tempGame.equalsGame(mGame);
					mGameHistory.push(tempGame);
					if (mGame.awayScore()) {
						mGame.newGame();
						updateServing();
					}
					updateView();
					return true;
				}
				
				else if (gesture == Gesture.SWIPE_LEFT){
					Game tempGame = new Game();
					tempGame.equalsGame(mGame);
					mGameHistory.push(tempGame);
					if (mGame.homeScore()) {
						mGame.newGame();
						updateServing();
					}
					updateView();
					return true;
				}
				
				return false;
			}
		});
		
		gestureDetector.setFingerListener(new GestureDetector.FingerListener() {
			@Override
			public void onFingerCountChanged(int previousCount, int currentCount) {
				// Finger count change
			}
		});
		
		return gestureDetector.setScrollListener(new GestureDetector.ScrollListener() {
			@Override
			public boolean onScroll (float displacement, float delta, float velocity) {
				return false;
				// DO IT
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tennis_main, menu);
		return true;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		mResumed = true;
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		mResumed = false;
	}
	
	@Override
	public boolean onGenericMotionEvent(MotionEvent event) {
		if (mGestureDetector != null) {
			return mGestureDetector.onMotionEvent(event);
		}
		return false;
	}
	
	public void updateView(){
		mHomeScoreView.setText(mGame.getHomeScore());
		mAwayScoreView.setText(mGame.getAwayScore()); 
		mHomeGameScoreView.setText(mGame.getHomeGamesWon()); 
		mAwayGameScoreView.setText(mGame.getAwayGamesWon());
		mHomeSetScoreView.setText(mGame.getHomeSetsWon());
		mAwaySetScoreView.setText(mGame.getAwaySetsWon());
	}
	
	class updateClock extends TimerTask{
		public void run() {
			long elapsedTime = mStartTime.getTime();
			hours = (int)(elapsedTime/(1000*60*60)%24);
			minutes = (int)(elapsedTime/(1000*60)%60);
			seconds = (int)(elapsedTime/(1000)%60);
		}
	}
	
	private Runnable mUpdateClock = new Runnable() {
		public void run() {
			long elapsedTime = mStartTime.getTime();
			int hours = (int)(elapsedTime/(1000*60*60)%24);
			int minutes = (int)(elapsedTime/(1000*60)%60);
			int seconds = (int)(elapsedTime/(1000)%60);
			mHours.setText(String.format("%02d", hours));
			mMinutes.setText(String.format("%02d", minutes));
			mSeconds.setText(String.format("%02d", seconds));
			
			mHandler.postDelayed(mUpdateClock, 250);
		}
	};
	
	private void updateServing(){
		if (mGame.isServing())
			mServingView.setRotation(90);
		
		else
			mServingView.setRotation(270);
	}

}
