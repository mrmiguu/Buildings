final
class Block {

/* Constants **************************/
private static final byte	DEFAULT_WIDTH	= 1,
				DEFAULT_HEIGHT	= 1;

static final byte		DIR_STRAIGHT	= 0,
				DIR_SIDEWAYS	= 1,
				DIR_UPRIGHT	= 2;

static final byte		POS_FLAT	= 0,
				POS_SIDE	= 1;

private static final byte	ASCII_OFFSET	= 32;

// general objects
static final byte		EMPTY		= (byte)' ' - ASCII_OFFSET,
				BOX		= (byte)'_' - ASCII_OFFSET,
				DOT		= (byte)'.' - ASCII_OFFSET;

// object specifics
private static final byte	BOX_HL		= (byte)'Y' - ASCII_OFFSET,
				BOX_H		= (byte)'Z' - ASCII_OFFSET,
				BOX_HR		= (byte)'[' - ASCII_OFFSET,
				BOX_VT		= (byte)'\\' - ASCII_OFFSET,
				BOX_V		= (byte)']' - ASCII_OFFSET,
				BOX_VB		= (byte)'^' - ASCII_OFFSET;

/* Immutables *************************/
final LWH	size;
final byte	character;

/* Mutables ***************************/
byte	direction;	// straight, sideways, upright
byte	position;	// flat, side
//``````````````````````````````````````````````````````````````````````````````

static
Block empty() {

	return new Block(new LWH(0, 0, 0), DOT);
}
//``````````````````````````````````````````````````````````````````````````````

Block() {

	this(new LWH(1, DEFAULT_WIDTH, DEFAULT_HEIGHT), BOX);
}
//``````````````````````````````````````````````````````````````````````````````

Block(
final byte length) {

	this(
	new	LWH(
		length,
		DEFAULT_WIDTH,
		DEFAULT_HEIGHT),
	BOX);
}
//``````````````````````````````````````````````````````````````````````````````

Block(
final LWH size,
final byte character) {

	this.size = size;
	this.character = character;
}
//``````````````````````````````````````````````````````````````````````````````

boolean isEmpty() {

	return size.length == 0 && size.width == 0 && size.height == 0;
}
}