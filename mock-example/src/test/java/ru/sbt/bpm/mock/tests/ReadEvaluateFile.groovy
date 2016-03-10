package ru.sbt.bpm.mock.tests
/**
 * @author sbt-bochev-as on 25.02.2016.
 *
 * Company: SBT - Moscow
 */

class RespObj {
    def properties = [:]

    def getProperty(String name) { properties[name] }

    void setProperty(String name, value) { properties[name] = value }
}

def response = new RespObj()

response.result = "\"test\"";

//out = new File()
def resource = getClass().classLoader.getResource("response.txt").text
def out = Eval.me("response", response.getProperties(), resource.replaceAll("\"", "\\\""))
assert out.toString().equals(response.result.toString())