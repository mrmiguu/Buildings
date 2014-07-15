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
final byte length,
final byte width,
final byte height) {

	this.length = length;
	this.width = width;
	this.height = height;
	volume = (short)(length * width * height);
}
}