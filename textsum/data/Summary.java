package textsum.data;


public class Summary {
	private int sentenceCountLimit;
	private String summary;
	
	public int getSentenceCountLimit() {
		return sentenceCountLimit;
	}
	public void setSentenceCountLimit(int sentenceCountLimit) {
		this.sentenceCountLimit = sentenceCountLimit;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}

	public void appendSummary(String summary){
		if(this.summary!=null)
		{
			this.summary= this.summary + summary;
		}
		else this.summary = summary;
	}
}
