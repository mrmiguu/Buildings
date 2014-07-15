import java.awt.Canvas;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;

final
class Main {

/* Constants **************************/
private static final String	TITLE		= "Buildings™";
private static final Color	BG_COLOR	= new Color(51, 153, 255);
private static final String	FONT_NAME	= "Courier";
private static final byte	FONT_WIDTH	= 12,
				FONT_HEIGHT	= 20;
private static final double	RATIO		= 4.0 / 1.5;
private static final byte	LINE_LENGTH	= 80,
				LINE_COUNT	= (byte)(LINE_LENGTH / RATIO);
private static final short	SCREEN_WIDTH	= LINE_LENGTH * FONT_WIDTH,
				SCREEN_HEIGHT	= LINE_COUNT * FONT_HEIGHT;

// character collection
private static final byte	ASCII_OFFSET	= 32;
private static final byte	EMPTY		= (byte)' ' - ASCII_OFFSET,
				BOX		= (byte)'\\' - ASCII_OFFSET,
				DOT		= (byte)'.' - ASCII_OFFSET;

/* Immutables *************************/
private static final Frame 		frame	= new Frame(TITLE);
private static final Canvas		canvas	= new Canvas();
private static final BufferedImage	font	= readImageFile(FONT_NAME);
private static final List<Byte>		board	= new ArrayList<>(LINE_LENGTH * LINE_COUNT);

/* Mutables ***************************/
private static boolean		unloading;
private static BufferStrategy	bufferStrategy;
private static byte		framesPerSecond;
private static int		cyclesPerSecond;
//``````````````````````````````````````````````````````````````````````````````

public static
void main(
final String[] args) {

	try {

	load();
	loop();
	unload();
	}
	catch (final Exception e) { e.printStackTrace(); }
}
//``````````````````````````````````````````````````````````````````````````````

private static
BufferedImage readImageFile(
final String filename) {

	try {

	return ImageIO.read(new File("img/" + filename.toLowerCase() + ".png"));
	}
	catch (final IOException e) {

	e.printStackTrace();
	return null;
	}
}
//``````````````````````````````````````````````````````````````````````````````

private static
void load()
throws Exception {

	canvas.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);

	frame.add(canvas);
	frame.setResizable(false);
	frame.pack();
	frame.addWindowListener(
		new WindowAdapter() {

		@Override
		public
		void windowClosing(
		final WindowEvent e) {

			unloading = true;
		}
		});

	canvas.setIgnoreRepaint(true);
	canvas.createBufferStrategy(2);

	bufferStrategy = canvas.getBufferStrategy();

	frame.setLocationRelativeTo(null);
	frame.setVisible(true);
}
//``````````````````````````````````````````````````````````````````````````````

private static
void draw()
throws Exception {

	final Graphics2D g = (Graphics2D)bufferStrategy.getDrawGraphics();
	g.clearRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

	for (byte y = (byte)(LINE_COUNT - 1); y >= 0; --y) {
	for (byte x = (byte)(LINE_LENGTH - 1); x >= 0; --x) {

	final short	left	= (short)(x * FONT_WIDTH),
			top	= (short)(y * FONT_HEIGHT),
			sx	= (short)(EMPTY * FONT_WIDTH);

	g.drawImage(
	font,
	left,			// left
	top,			// top
	left + FONT_WIDTH,	// right
	top + FONT_HEIGHT,	// bottom
	sx,
	0,
	sx + FONT_WIDTH,
	FONT_HEIGHT,
	null);

	}
	}

	g.dispose();
	bufferStrategy.show();
}
//``````````````````````````````````````````````````````````````````````````````

private static
void loop()
throws Exception {

	long	timer15		= 0,
		timer1000	= 0,
		timer2500	= 0;
	byte	frameCounter	= 0;
	int	cycleCounter	= 0;

	while (true) {

	final long NOW = System.currentTimeMillis();

	if (timer2500 < NOW) {
		if (unloading) return;
		timer2500 = NOW + 2500;
	}

	if (timer15 < NOW) {
		draw();
		++frameCounter;
		timer15 = NOW + 15;
	}

	++cycleCounter;

	if (timer1000 < NOW) {
		System.out.println(cycleCounter);
		framesPerSecond = frameCounter;
		cyclesPerSecond = cycleCounter;
		cycleCounter = frameCounter = 0;
		timer1000 = NOW + 1000;
	}
	}
}
//``````````````````````````````````````````````````````````````````````````````

private static
void unload()
throws Exception {

	font.flush();
	bufferStrategy.dispose();
	frame.dispose();
}
}