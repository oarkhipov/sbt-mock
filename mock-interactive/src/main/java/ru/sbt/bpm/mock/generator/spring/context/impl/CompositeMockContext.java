package ru.sbt.bpm.mock.generator.spring.context.impl;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.sbt.bpm.mock.generator.spring.context.IMockContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sbt-hodakovskiy-da on 22.06.2016.
 */
@Slf4j
@NoArgsConstructor
public class CompositeMockContext implements IMockContext {

	private List<IMockContext> listContext = new ArrayList<IMockContext>();

	@Override
	public String getContext () {
		StringBuilder sb = new StringBuilder();
		for (IMockContext mockContext : listContext)
			sb.append(mockContext.getContext());

		return sb.toString();
	}

	public void add(IMockContext mockContext) {
		listContext.add(mockContext);
	}

	public void remove(IMockContext mockContext) {
		listContext.remove(mockContext);
	}
}
