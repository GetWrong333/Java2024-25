import java.util.*;

public class CanvasFill extends Fill {

    @Override
    public void fill(Canvas canvas, List<Position2D> neighbours, Position2D start, int brightness) {
        // Maksymalne wymiary płótna
        Position2D maxPosition = canvas.getMaxPosition();
        int maxCol = maxPosition.getCol();
        int maxRow = maxPosition.getRow();

        // Sprawdzenie początkowego warunku jasności
        if (canvas.getBrightness(start) >= brightness) return;

        // Tablica odwiedzonych pikseli
        boolean[][] visited = new boolean[maxRow + 1][maxCol + 1];

        // Lista do przetwarzania pikseli (zamiast klasycznej kolejki)
        List<Position2D> toVisit = new ArrayList<>();
        toVisit.add(start);
        visited[start.getRow()][start.getCol()] = true;

        // Przetwarzanie pikseli
        for (int i = 0; i < toVisit.size(); i++) {
            Position2D current = toVisit.get(i);

            // Ustaw jasność bieżącego piksela
            canvas.setBrightness(current, brightness);

            // Dla każdego sąsiada
            for (Position2D neighbourOffset : neighbours) {
                int newCol = current.getCol() + neighbourOffset.getCol();
                int newRow = current.getRow() + neighbourOffset.getRow();

                // Sprawdzanie granic płótna
                if (newCol >= 0 && newCol <= maxCol && newRow >= 0 && newRow <= maxRow) {
                    // Sprawdzanie, czy piksel był odwiedzony
                    if (!visited[newRow][newCol]) {
                        Position2D newPosition = new Position2D(newCol, newRow);
                        int currentBrightness = canvas.getBrightness(newPosition);

                        // Dodanie piksela, jeśli spełnia warunek jasności
                        if (currentBrightness < brightness) {
                            toVisit.add(newPosition);
                            visited[newRow][newCol] = true;
                        }
                    }
                }
            }
        }
    }
}
