import java.awt.Canvas;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
private static final double	RATIO		= 4.0 / 3.0;
private static final byte	LINE_LENGTH	= 40,				// x
				LINE_COUNT	= (byte)(LINE_LENGTH / RATIO),	// y
				LINE_STACK	= LINE_COUNT;			// z
private static final short	SCREEN_WIDTH	= LINE_LENGTH * FONT_WIDTH << 1,
				SCREEN_HEIGHT	= LINE_COUNT * FONT_HEIGHT;

// character collection
private static final byte	ASCII_OFFSET	= 32;
private static final byte	EMPTY		= (byte)' ' - ASCII_OFFSET,
				BOX_HL		= (byte)'Y' - ASCII_OFFSET,
				BOX_H		= (byte)'Z' - ASCII_OFFSET,
				BOX_HR		= (byte)'[' - ASCII_OFFSET,
				BOX_VT		= (byte)'\\' - ASCII_OFFSET,
				BOX_V		= (byte)']' - ASCII_OFFSET,
				BOX_VB		= (byte)'^' - ASCII_OFFSET,
				BOX		= (byte)'_' - ASCII_OFFSET,
				DOT		= (byte)'.' - ASCII_OFFSET;

/* Immutables *************************/
private static final Frame 		frame	= new Frame(TITLE);
private static final Canvas		canvas	= new Canvas();
private static final BufferedImage	font	= readImageFile(FONT_NAME);
private static final byte[][][][]	board	=
	new byte
	[2]		// left/right side
	[LINE_LENGTH]	// x
	[LINE_COUNT]	// y
	[LINE_STACK];	// z

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
short getMouseX() {

	return	(short)(MouseInfo.getPointerInfo().getLocation().getX()
		- canvas.getLocationOnScreen().getX());
}
//``````````````````````````````````````````````````````````````````````````````
        
private static
short getMouseY() {

	return	(short)(MouseInfo.getPointerInfo().getLocation().getY()
		- canvas.getLocationOnScreen().getY());
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

	for (byte z = (byte)(LINE_STACK - 1); z >= 0; --z) {
	for (byte y = (byte)(LINE_COUNT - 1); y >= 0; --y) {
	for (byte x = (byte)(LINE_LENGTH - 1); x >= 0; --x) {
	for (byte s = 1; s >= 0; --s) {

	if (x == LINE_LENGTH - 1) {
		if (y <= LINE_COUNT - 1 && y >= 0) {
			if (y == LINE_COUNT - 1) {
				board[s][x][y][z] = BOX_VB;
				continue;
			}
			else if (y == 0) {
				board[s][x][y][z] = BOX_VT;
				continue;
			}
			else {
				board[s][x][y][z] = BOX_V;
				continue;
			}
		}
	}

	board[s][x][y][z] = DOT;
	}
	}
	}
	}
}
//``````````````````````````````````````````````````````````````````````````````

private static
void draw()
throws Exception {

	final Graphics2D g = (Graphics2D)bufferStrategy.getDrawGraphics();
	g.setBackground(BG_COLOR);
	g.clearRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

	for (byte z = (byte)(LINE_STACK - 1); z >= 0; --z) {
	for (byte y = (byte)(LINE_COUNT - 1); y >= 0; --y) {
	for (byte x = (byte)(LINE_LENGTH - 1); x >= 0; --x) {
	for (byte s = 1; s >= 0; --s) {

	final short	left	= (short)((s * LINE_LENGTH * FONT_WIDTH) + x * FONT_WIDTH),
			top	= (short)(y * FONT_HEIGHT),
			tile	= (short)(board[s][x][y][z] * FONT_WIDTH);

	g.drawImage(
	font,
	left,			// left
	top,			// top
	left + FONT_WIDTH,	// right
	top + FONT_HEIGHT,	// bottom
	tile,
	0,
	tile + FONT_WIDTH,
	FONT_HEIGHT,
	null);

	}
	}
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

	for (byte z = (byte)(LINE_STACK - 1); z >= 0; --z) {
	for (byte y = (byte)(LINE_COUNT - 1); y >= 0; --y) {
	for (byte x = (byte)(LINE_LENGTH - 1); x >= 0; --x) {
	for (byte s = 1; s >= 0; --s) {

		board[s][x][y][z] = 0;
	}
	}
	}
	}

	font.flush();
	bufferStrategy.dispose();
	frame.dispose();
}
}