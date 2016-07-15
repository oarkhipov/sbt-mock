package ru.sbt.bpm.mock.spring.context.generator;

import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class CommentMarshalListener extends Marshaller.Listener {
    private final XMLStreamWriter xmlStreamWriter;

    public CommentMarshalListener (XMLStreamWriter xmlStreamWriter) {
        this.xmlStreamWriter = xmlStreamWriter;
    }

    @Override
    public void beforeMarshal (Object source) {
//        super.beforeMarshal(source);
        try {
            if (source instanceof AbstractCommentable) {
                final String comment = ((AbstractCommentable) source).getComment();
                if (comment!= null && !comment.isEmpty()) {
                    xmlStreamWriter.writeComment(comment);
                }
            }
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }
}