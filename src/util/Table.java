package util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * https://github.com/2xsaiko/crogamp/blob/master/src/com/github/mrebhan/crogamp/cli/TableList.java
 */
public class Table {
	public static <T> void table(List<T[]> rows, boolean leftJustified, int columnWidth, boolean wrap) {
		// Calculate the appropriate length of each column.
		Map<Integer, Integer> columnLengths = new HashMap<>();
		rows.stream().forEach(a -> Stream.iterate(0, (i -> i < a.length), (i -> ++i)).forEach(i -> {
			if (columnLengths.get(i) == null) {
				columnLengths.put(i, 0);
			}
			if (columnLengths.get(i) < ((CharSequence) a[i]).length()) {
				columnLengths.put(i, ((CharSequence) a[i]).length());
			}
		}));

		// Prepare the format string.
		final StringBuilder formatString = new StringBuilder("");
		String flag = leftJustified ? "-" : "";
		columnLengths.entrySet().stream().forEach(e -> formatString.append("| %" + flag + e.getValue() + "s "));
		formatString.append("|\n");

		// Prepare the line for the top, bottom, and below the header row.
		String line = columnLengths.entrySet().stream().reduce("", (ln, b) -> {
			String templn = "+-";
			templn = templn
					+ Stream.iterate(0, (i -> i < b.getValue()), (i -> ++i)).reduce("", (ln1, b1) -> ln1 + "-",
							(a1, b1) -> a1 + b1);
			templn = templn + "-";
			return ln + templn;
		}, (a, b) -> a + b);
		line = line + "+\n";

		// Print the table.
		System.out.print(line);
		rows.stream().forEach(a -> System.out.printf(formatString.toString(), a));
		System.out.print(line);
	}

}