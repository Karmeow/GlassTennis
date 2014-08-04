package com.kevinkarabon.glasstennis;

import java.lang.String;
import java.util.Random;

import android.widget.TextView;

public class Game {
	
	public enum TennisScore {
		Love, Five, Three, Four, Ad
	}

	private int homeScore;
	private int awayScore;
	private boolean mDeuce;
	private int homeGamesWon;
	private int awayGamesWon;
	private int homeSetsWon;
	private int awaySetsWon;
	
	// false - Away is serving
	// true - Home is serving
	private boolean serving;
	
	public Game() {
		mDeuce = false;
		homeScore = 0;
		awayScore = 0;
		homeGamesWon = 0;
		awayGamesWon = 0;
		homeSetsWon = 0;
		awaySetsWon = 0;
		pickServer();
	}
	
	public void pickServer() {
		Random rand = new Random();
		int randomNum = rand.nextInt((1-0) + 1);
		if (randomNum == 0)
			serving = false;

		else
			serving = true;
	}
	
	public void newGame() {
		mDeuce = false;
		homeScore = 0;
		awayScore = 0;
		serving = !serving;
	}
	
	// Function: homeScore()
	// Returns:
	//			True - Game won for you!
	//			False - Game still ongoing
	public boolean homeScore() {
		homeScore++;
		
		if (homeScore == 3 && awayScore == 3){
			mDeuce = true;
			return false;
		}
		
		else if (homeScore < 4 && mDeuce == false) 
			return false;	
		
		else if (homeScore < 5 && mDeuce == true) {
			if (awayScore == 4)
				{ homeScore--; awayScore--; }
			return false;
		}
		
		homeGamesWon++;
		if (homeGamesWon == 6) {
			homeSetsWon++;
			awayGamesWon = 0;
			homeGamesWon = 0;
		}
			
			
		return true;
	}
	
	// Function: awayScore()
		// Returns:
		//			True - Game won for opposing player
		//			False - Game still ongoing
	public boolean awayScore() {
		awayScore++;
		
		if (homeScore == 3 && awayScore == 3){
			mDeuce = true;
			return false;
		}
		
		else if (awayScore < 4 && mDeuce == false) 
			return false;	
		
		else if (awayScore < 5 && mDeuce == true) {
			if (homeScore == 4)
				{ homeScore--; awayScore--; }
			return false;
		}
		
		awayGamesWon++;
		if (awayGamesWon == 6) {
			awaySetsWon++;
			awayGamesWon = 0;
			homeGamesWon = 0;
		}

		return true;
	}
	
	public boolean isDeuce() {
		return mDeuce;
	}
	
	public String getAwayScore() {
		return getValue(awayScore);
	}
	
	public String getHomeScore() {
		return getValue(homeScore);
	}
	
	public String getHomeGamesWon() {
		return Integer.toString(homeGamesWon);
	}

	public String getAwayGamesWon() {
		return Integer.toString(awayGamesWon);
	}

	public String getHomeSetsWon() {
		return Integer.toString(homeSetsWon);
	}

	public String getAwaySetsWon() {
		return Integer.toString(awaySetsWon);
	}
	
	public boolean isServing() {
		return serving;
	}

	public void equalsGame(Game game){
		this.awayScore = game.awayScore;
		this.homeScore = game.homeScore;
		this.awayGamesWon = game.awayGamesWon;
		this.homeGamesWon = game.homeGamesWon;
		this.awaySetsWon = game.awaySetsWon;
		this.homeSetsWon = game.homeGamesWon;
		this.mDeuce = game.mDeuce;
		this.serving = game.serving;
	}

	private String getValue(int score){
		String str_result = "0";
		if (!mDeuce) {
			switch (score) {
				case 1: str_result = "15";
						break;
				case 2: str_result = "30";
						break;
				case 3: str_result = "40";
						break;
			}
		}
		
		else {
			if (homeScore == 3 && awayScore == 3){
				str_result = "40";
			}
			
			else{
				switch (score) {
				case 3: str_result = "-";
						break;
				case 4: str_result = "Ad";
						break;
				}
			}
		}
		
		return str_result;
	}
}