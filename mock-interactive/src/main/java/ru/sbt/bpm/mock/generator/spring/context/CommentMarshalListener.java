package ru.sbt.bpm.mock.generator.spring.context;

import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.PrintStream;
import java.io.StringWriter;

/**
 * @author sbt-bochev-as on 13.01.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */
public class CommentMarshalListener extends Marshaller.Listener {

    private final XMLStreamWriter xmlStreamWriter;

    public CommentMarshalListener(XMLStreamWriter xmlStreamWriter) {
        this.xmlStreamWriter = xmlStreamWriter;
    }

    @Override
    public void beforeMarshal(Object source) {
//        super.beforeMarshal(source);
        try {
            if (source instanceof ContextCommentable) {
                final String comment = ((ContextCommentable) source).getComment();
                if (comment!= null && !comment.isEmpty()) {
                    xmlStreamWriter.writeComment(comment);
                }
            }
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }
}
