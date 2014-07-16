final
class XYZ {

/* Constants **************************/
private static final byte	DEFAULT_X	= 0,
				DEFAULT_Y	= 0,
				DEFAULT_Z	= 0;

/* Mutables ***************************/
byte	x,
	y,
	z;
//``````````````````````````````````````````````````````````````````````````````

XYZ() {

	this(
	DEFAULT_X,
	DEFAULT_Y,
	DEFAULT_Z);
}
//``````````````````````````````````````````````````````````````````````````````

XYZ(
final int x,
final int y,
final int z) {

	this.x = (byte)x;
	this.y = (byte)y;
	this.z = (byte)z;
}
}