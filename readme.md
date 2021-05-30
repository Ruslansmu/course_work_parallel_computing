# Description

When performing this work, a program was created that reads
the .text file and builds an inverted index. An inverted index
is also called a search index, with the help of it you can
determine in which documents the words are stored.

# Installation

```sh
git clone https://github.com/Ruslansmu/course_work_parallel_computing
```
 - if you need, you can increase the list of stop words

Compile
```sh
compile this in your IDE to get the output
```
# Launching

- first you need to start the server
- and only then launch the client


# Configuring
- Server (Server.java)
  ```sh
      server = new ServerSocket(4004);
  ```

    - Number of threads
  ```sh
      public static int NUMBER_THREADS = 4;
    ```


- Client (Client.java)
    - Ip and Port
  ```sh
      clientSocket = new Socket("localhost", 4004);
  ```
