package game;

public enum Direction {
	UP		( Math.PI/2),
	DOWN	(-Math.PI/2),
	LEFT	(Math.PI),
	RIGHT	(0);
	
	
	private double directionAsDouble;

	private Direction( double direction ) {
		directionAsDouble = direction;
	}
	
	public double asDouble() {
		return directionAsDouble;
	}
}
