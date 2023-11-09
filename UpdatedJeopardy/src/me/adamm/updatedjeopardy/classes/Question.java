package me.adamm.updatedjeopardy.classes;

public class Question {
	
	private String question;
	private String answer;
	private String category;
	private String image;
	
	private int worth;
	private boolean isDailyDouble;
	
	public Question(String q, String a, String c, int w, boolean idd) {
		this.question = q;
		this.answer = a;
		this.category = c;
		
		this.worth = w;
		this.isDailyDouble = idd;
		this.image = null;
	}

	public Question(String q, String a, String c, int w, boolean idd, String image) {
		this.question = q;
		this.answer = a;
		this.category = c;
		
		this.worth = w;
		this.isDailyDouble = idd;
		this.image = image;
	}	
	
	public String getQuestion() {
		return this.question;
	}

	public String getAnswer() {
		return this.answer;
	}

	public String getCategory() {
		return this.category;
	}

	public int getWorth() {
		return this.worth;
	}
	
	public Boolean isImageQuestion() {
		return !(this.image == null);
	}
	
	public String getImageTemplate() {
		return this.image;
	}
	
	public void setWorth(int nworth) {
		this.worth = nworth;
	}

	public boolean isDailyDouble() {
		return this.isDailyDouble;
	}
}
