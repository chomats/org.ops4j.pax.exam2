package org.ops4j.pax.exam;

/**
 * Created by IntelliJ IDEA.
 * User: tonit
 * Date: 3/16/11
 * Time: 10:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestInstantiationInstruction {

    final private String m_instruction;

    public TestInstantiationInstruction( String s )
    {
        m_instruction = s;
    }

    public String toString() {
        return m_instruction;
    }
}
