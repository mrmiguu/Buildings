final
class LWH {

/* Constants **************************/
private static final byte	DEFAULT_LENGTH	= 1,
				DEFAULT_WIDTH	= 1,
				DEFAULT_HEIGHT	= 1;

/* Mutables ***************************/
final byte	length,
		width,
		height;
final short	volume;
//``````````````````````````````````````````````````````````````````````````````

LWH() {

	this(
	DEFAULT_LENGTH,
	DEFAULT_WIDTH,
	DEFAULT_HEIGHT);
}
//``````````````````````````````````````````````````````````````````````````````

LWH(
final int length,
final int width,
final int height) {

	this.length = (byte)length;
	this.width = (byte)width;
	this.height = (byte)height;
	volume = (short)(length * width * height);
}
}