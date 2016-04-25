package ru.sbt.bpm.mock.logging.utils;

import org.springframework.data.domain.Sort;

import java.util.Collection;

/**
 * @author sbt-bochev-as on 24.04.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */
public class SortUtils {
    public static Sort allOf(Sort... sorts) {
        Sort resultSort = null;
        for (Sort sort : sorts) {
            if (resultSort == null) {
                resultSort = sort;
            } else {
                resultSort = resultSort.and(sort);
            }
        }
        return resultSort;
    }

    public static Sort allOf(Collection<Sort> sorts) {
        Sort resultSort = null;
        for (Sort sort : sorts) {
            if (resultSort == null) {
                resultSort = sort;
            } else {
                resultSort = resultSort.and(sort);
            }
        }
        return resultSort;
    }
}
