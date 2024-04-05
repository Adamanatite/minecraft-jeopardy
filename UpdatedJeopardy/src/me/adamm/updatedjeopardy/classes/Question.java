package me.adamm.updatedjeopardy.classes;

/**
 * Represents a question in the jeopardy game
 * 
 * @author Adam Fairlie <adamfairlie01@gmail.com>
 */
public class Question {
	
	/* Question information */
	private String question;
	private String answer;
	private String category;
	private String image;
	private int worth;
	private boolean isDailyDouble;
	
	/**
	 * Creates a new (non-image) Question object
	 * 
	 * @param q The question text
	 * @param a The text of the answer to the question
	 * @param c The text of the category name of the question
	 * @param w The worth of the question in points/dollars
	 * @param idd If the question is a daily double
	 */
	public Question(String q, String a, String c, int w, boolean idd) {
		this.question = q;
		this.answer = a;
		this.category = c;
		
		this.worth = w;
		this.isDailyDouble = idd;
		this.image = null;
	}

	/**
	 * Creates a new Image Question object
	 * 
	 * @param q The question text
	 * @param a The text of the answer to the question
	 * @param c The text of the category name of the question
	 * @param w The worth of the question in points/dollars
	 * @param idd If the question is a daily double
	 * @param image The name of the image template in the config
	 */
	public Question(String q, String a, String c, int w, boolean idd, String image) {
		this.question = q;
		this.answer = a;
		this.category = c;
		
		this.worth = w;
		this.isDailyDouble = idd;
		this.image = image;
	}	
	
	/**
	 * Getter method for the question text
	 * 
	 * @return question The question text
	 */
	public String getQuestion() {
		return this.question;
	}

	/**
	 * Getter method for the answer text
	 * 
	 * @return answer The answer text
	 */
	public String getAnswer() {
		return this.answer;
	}

	/**
	 * Getter method for the category text
	 * 
	 * @return category The category text
	 */
	public String getCategory() {
		return this.category;
	}

	/**
	 * Getter method for the question worth
	 * 
	 * @return worth The question worth
	 */
	public int getWorth() {
		return this.worth;
	}
	
	/**
	 * Returns if the question is an image question
	 * 
	 * @return isImage If the question is an image question
	 */
	public Boolean isImageQuestion() {
		// Check if the image field is null or has an image
		return !(this.image == null);
	}
	
	/**
	 * Getter method for the image template name
	 * 
	 * @return image The image template name
	 */
	public String getImageTemplate() {
		return this.image;
	}
	
	/**
	 * Setter method for the question worth
	 * 
	 * @param worth The new worth of the question
	 */
	public void setWorth(int nworth) {
		this.worth = nworth;
	}

	/**
	 * Getter method for if the question is a daily double
	 * 
	 * @return isDailyDouble If the question is a daily double
	 */
	public boolean isDailyDouble() {
		return this.isDailyDouble;
	}
}
