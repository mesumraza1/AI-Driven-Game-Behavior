package pathfind;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class keyhandler implements KeyListener
{
	demopanel dp;
	
	public keyhandler(demopanel dp)
	{
		this.dp=dp;
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) 
	{
		// TODO Auto-generated method stub
		int code=e.getKeyCode();
		if(code==KeyEvent.VK_ENTER)
		{
//			dp.search();
			dp.autoSearch();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
