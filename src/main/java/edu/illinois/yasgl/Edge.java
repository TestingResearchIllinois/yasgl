/* The MIT License (MIT)

Copyright (c) 2016 Alex Gyori

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package edu.illinois.yasgl;

public class Edge<V> {

    private V source;
    private V destination;

    public Edge(V source, V destination) {
        this.source = source;
        this.destination = destination;
    }

    public V getSource() {
        return this.source;
    }

    public V getDestination() {
        return this.destination;
    }

    public boolean equals(Object o) {
        if (o instanceof Edge) {
            Edge e = (Edge) o;
            return this.source.equals(e.source) && this.destination.equals(e.destination);
        }
        return false;
    }

    public int hashCode() {
        return this.source.hashCode() + this.destination.hashCode();
    }

    public String toString() {
        return this.source + " --> " + this.destination;
    }
}
