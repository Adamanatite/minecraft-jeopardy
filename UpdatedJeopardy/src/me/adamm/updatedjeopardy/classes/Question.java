package me.adamm.updatedjeopardy.classes;

public class Question {
	
	private String question;
	private String answer;
	private String category;
	
	private int worth;
	private boolean isDailyDouble;
	
	public Question(String q, String a, String c, int w, boolean idd) {
		this.question = q;
		this.answer = a;
		this.category = c;
		
		this.worth = w;
		this.isDailyDouble = idd;
	}

	public String getQuestion() {
		return question;
	}

	public String getAnswer() {
		return answer;
	}

	public String getCategory() {
		return category;
	}

	public int getWorth() {
		return worth;
	}
	
	public void setWorth(int nworth) {
		this.worth = nworth;
	}

	public boolean isDailyDouble() {
		return isDailyDouble;
	}
}
