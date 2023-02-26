gcc -Dcheck_leak_native_EXPORTS -I. -I$JAVA_HOME/include -I$JAVA_HOME/include/linux -g -std=gnu11 -Wall  -fPIC -o agent.c.o -c agent.c
gcc -fPIC -g -std=gnu11 -Wall  -shared -Wl,-soname,libcheckleak.so -o ../resources/platforms-lib/linux-amd64/libcheckleak.so agent.c.o 
