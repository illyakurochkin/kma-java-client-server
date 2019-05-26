package server;

import protocol.Package;

public interface Processor {
    void process(Package pack);
}
