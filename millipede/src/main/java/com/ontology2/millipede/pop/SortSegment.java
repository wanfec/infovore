package com.ontology2.millipede.pop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.ontology2.millipede.LineMultiFile;
import com.ontology2.millipede.Plumbing;
import com.ontology2.millipede.sink.Sink;
import com.ontology2.millipede.source.Source;

public class SortSegment<T> implements Sink<T> {
	private final Comparator<? super T> comparator;
	private final Sink<T> innerSink;
	private final ArrayList<T> sortMe;

	public SortSegment(Comparator<? super T> comparator,Sink<T> innerSink) {
		this.comparator = comparator;
		this.innerSink= innerSink;
		sortMe=new ArrayList<T>();
	}

	@Override
	public void accept(T obj) throws Exception {
		sortMe.add(obj);
	}

	@Override
	public void close() throws Exception {
		Collections.sort(sortMe,comparator);
		Plumbing.drain(sortMe, innerSink);
		innerSink.close();
	}
}
