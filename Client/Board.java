final
class Board {

/* Constants **************************/

/* Immutables *************************/
final LWH			size;

/*
 * Depending on the size, angle, and
 * direction of the block, other spots
 * on the game board may be taken up due
 * to 3-dimensional space the block
 * occupies because of its physical
 * properties
 */
private final Block[][][]	space;

/* Mutables ***************************/
//``````````````````````````````````````````````````````````````````````````````

Board(
final LWH size) {

	this.size = size;

	// make sure width is x, height is y, and length is z
	space = new Block[size.width][size.height][size.length];

	// clear the board for block placement
	for (byte z = (byte)(size.length - 1); z >= 0; --z) {
	for (byte y = (byte)(size.height - 1); y >= 0; --y) {
	for (byte x = (byte)(size.width - 1); x >= 0; --x) {

	space[x][y][z] = Block.empty();
	}
	}
	}
}
//``````````````````````````````````````````````````````````````````````````````

Block getBlock(
final XYZ src) {

	return space[src.x][src.y][src.z];
}
//``````````````````````````````````````````````````````````````````````````````

void place(
final Block b,
final XYZ dest) {

	if (!space[dest.x][dest.y][dest.z].isEmpty()) throw new RuntimeException("Can't place it there");
	space[dest.x][dest.y][dest.z] = b;
}
//``````````````````````````````````````````````````````````````````````````````

void move(
final XYZ src,
final XYZ dest) {

	if (!space[dest.x][dest.y][dest.z].isEmpty()) throw new RuntimeException("Can't move it there");
	if (space[src.x][src.y][src.z].isEmpty()) throw new RuntimeException("Nothing to move");

	space[dest.x][dest.y][dest.z] = space[src.x][src.y][src.z];
	space[src.x][src.y][src.z] = null;
}
}