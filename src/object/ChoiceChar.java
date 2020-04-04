package object;

import java.awt.Image;

import pub.Quiz;

public class ChoiceChar extends Obj {
	private Quiz.QUIZ quiz;
	
	public ChoiceChar(Image image, Quiz.QUIZ quiz,int xx, int yy) {
		super(image);
		x = (app.getGUIManager().getWidth() / 60 * 4) + ((app.getGUIManager().getWidth() / 50 * 6) * xx );
		y = app.getGUIManager().getHeight() / 50 * 17 + ((app.getGUIManager().getHeight() / 50 * 8) * yy );
		this.quiz = quiz;
	}

	@Override
	public void move() {
		
	}
	
	@Override
	public boolean checkHit(Obj c) {
		if (c instanceof Bullet1) {
			if (super.checkHit(c)) {
				app.getMaterials().playSE(app.getMaterials().snd_Bang);
				dead();
				c.dead();
				return true;
			}
		}
		return false;
	}
	
	public Quiz.QUIZ getQuiz() {
		return quiz;
	}
	
}