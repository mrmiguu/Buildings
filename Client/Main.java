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
private static final String	TITLE		= "Buildings�";

private static final String	FONT_NAME	= "Courier";
private static final byte	FONT_WIDTH	= 12,
				FONT_HEIGHT	= 20;

private static final double	RATIO		= 4.0 / 3.0;

private static final byte	LINE_LENGTH	= 40,				// x
				LINE_COUNT	= (byte)(LINE_LENGTH / RATIO),	// y
				LINE_STACK	= LINE_COUNT;			// z

private static final short	CAMERA_WIDTH	= LINE_LENGTH * FONT_WIDTH,
				CAMERA_HEIGHT	= LINE_COUNT * FONT_HEIGHT;

private static final Color	BG_COLOR	= new Color(51, 153, 255);
private static final short	SCREEN_WIDTH	= CAMERA_WIDTH << 1,
				SCREEN_HEIGHT	= CAMERA_HEIGHT;

// block collection
private static final Block	BLOCK_1x1_1	= new Block(),
				BLOCK_1x1_2	= new Block((byte)2),
				BLOCK_1x1_12	= new Block((byte)12);

/* Immutables *************************/
private static final Frame 		frame	= new Frame(TITLE);
private static final Canvas		canvas	= new Canvas();

private static final BufferedImage	font	= readImageFile(FONT_NAME);
//private static final BufferedImage[]	layer	= new BufferedImage[LINE_STACK];

static final Board[]			board	=
	new Board[] {

	new Board( // normal straight-on camera
	new	LWH(
		LINE_STACK,
		LINE_LENGTH,
		LINE_COUNT)),
	new Board( // length and height swap for this top-down camera
	new	LWH(
		LINE_COUNT,
		LINE_LENGTH,
		LINE_STACK))
	};

/* Mutables ***************************/
private static boolean		unloading;

private static BufferStrategy	bufferStrategy;
private static BufferedImage	background,
				middleground,
				foreground;

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
void place(
final Block b,
final XYZ dest) {

	board[0].place(b, new XYZ(dest.x, dest.y, dest.z)); // straight-on
	board[1].place(b, new XYZ(dest.x, dest.z, dest.y)); // top-down
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

	canvas.addMouseListener(
		new MouseAdapter() {

		@Override
		public void mouseClicked(
		final MouseEvent e) {

			
		}
		});
	canvas.setIgnoreRepaint(true);
	canvas.createBufferStrategy(2);

	bufferStrategy = canvas.getBufferStrategy();

	frame.setLocationRelativeTo(null);
	frame.setVisible(true);

	place(BLOCK_1x1_1, new XYZ(0, 0, 0));
	place(BLOCK_1x1_1, new XYZ(10, 15, 20));
	place(BLOCK_1x1_1, new XYZ(20, 15, 10));
	place(BLOCK_1x1_1, new XYZ(LINE_LENGTH - 2, LINE_COUNT - 1, LINE_STACK - 6));

	unlockMiddleground(LINE_STACK - 6);
	//loadLayers();
}
//``````````````````````````````````````````````````````````````````````````````

private static
void unlockMiddleground(
final int layer) {

	/*
	 * Lock the layers behind to the
	 * background and the layers in front
	 * to the foreground
	 */

	background =
		new BufferedImage(
			SCREEN_WIDTH,
			SCREEN_HEIGHT,
			BufferedImage.TYPE_INT_ARGB);
	middleground =
		new BufferedImage(
			SCREEN_WIDTH,
			SCREEN_HEIGHT,
			BufferedImage.TYPE_INT_ARGB);
	foreground =
		new BufferedImage(
			SCREEN_WIDTH,
			SCREEN_HEIGHT,
			BufferedImage.TYPE_INT_ARGB);

	final Graphics2D bg = (Graphics2D)background.getGraphics();
	final Graphics2D mg = (Graphics2D)middleground.getGraphics();
	final Graphics2D fg = (Graphics2D)foreground.getGraphics();

	for (byte c = 0; c < board.length; ++c) {
	for (byte l = 0; l < LINE_STACK; ++l) {
	for (byte y = (byte)(LINE_COUNT - 1); y >= 0; --y) {
	for (byte x = (byte)(LINE_LENGTH - 1); x >= 0; --x) {

	final short	left	= (short)(c * LINE_LENGTH * FONT_WIDTH + x * FONT_WIDTH),
			top	= (short)(y * FONT_HEIGHT),
			tile	= (short)(board[c].getBlock(new XYZ(x, y, l)).character * FONT_WIDTH);

	if (l > layer)
		fg.drawImage(
			font, left, top, left + FONT_WIDTH, top + FONT_HEIGHT,
			tile, 0, tile + FONT_WIDTH, FONT_HEIGHT, null);
	else if (l < layer)
		bg.drawImage(
			font, left, top, left + FONT_WIDTH, top + FONT_HEIGHT,
			tile, 0, tile + FONT_WIDTH, FONT_HEIGHT, null);
	else	mg.drawImage(
			font, left, top, left + FONT_WIDTH, top + FONT_HEIGHT,
			tile, 0, tile + FONT_WIDTH, FONT_HEIGHT, null);
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

	g.drawImage(background, 0, 0, null);
	g.drawImage(middleground, 0, 0, null);
	g.drawImage(foreground, 0, 0, null);

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

	//board[0]
	//board[1]

	font.flush();
	bufferStrategy.dispose();
	frame.dispose();
}
}