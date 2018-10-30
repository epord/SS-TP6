package CellIndexMethod;

import Models.Particle;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class CellGrid {

	private Cell[][] cells;

	private Double width;
	private Double height;
	private Double cellSize;
	private Integer rows;
	private Integer cols;

	public CellGrid(Double width, Double height, Double cellSize) throws IllegalArgumentException {

		Double delta = 0.00001;

		if (Math.abs(width / cellSize) % 1 > delta || Math.abs(height / cellSize) % 1 > delta) {
			throw new IllegalArgumentException("Width or Height not multiple of cellSize");
		}

		this.width = width;
		this.height = height;
		this.cellSize = cellSize;

		this.rows = (int) (height / cellSize);
		this.cols = (int) (width / cellSize);
		generateGrid();
	}

	private void generateGrid() {
		this.cells = new Cell[rows][cols];
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				cells[row][col] = new Cell();
			}
		}
	}

	public void clear() {
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				cells[row][col].clear();
			}
		}
	}

	public Double getWidth() {
		return width;
	}

	public Double getHeight() {
		return height;
	}

	public List<Particle> getAdjacentParticles(Particle particle) {
		int centerRow = (int) Math.floor(particle.getPosition().getY() / cellSize);
		int centerCol = (int) Math.floor(particle.getPosition().getX() / cellSize);

		List<Particle> adjacent = new LinkedList<>();

		for (int row = centerRow - 1; row <= centerRow + 1; row++) {
			for (int col = centerCol - 1; col <= centerCol + 1; col++) {
				if (row >= 0 && row < rows && col >= 0 && col < cols) {
					adjacent.addAll(cells[row][col].getParticles());
				}
			}
		}

		adjacent.remove(particle);

		return adjacent;
	}

	public void addParticle(Particle particle) {
		int row = (int) Math.floor(particle.getPosition().getY() / cellSize);
		int col = (int) Math.floor(particle.getPosition().getX() / cellSize);

		if (row >= 0 && row < rows && col >= 0 && col < cols)
			cells[row][col].addParticle(particle);
	}

	// TODO: Test if this works properly, or make CellIndexMethod.Cell list a Concurrent List
	public void addParticles(Collection<Particle> particles) {
		particles.stream().forEach(p -> addParticle(p));
	}

	public List<Particle> getOutsideParticles(Collection<Particle> particles) {

		return particles.parallelStream().filter(particle -> particle.getPosition().getX() < 0.0 ||
				particle.getPosition().getX() > width || particle.getPosition().getY() < 0.0 ||
				particle.getPosition().getY() > height).collect(Collectors.toList());
	}

}