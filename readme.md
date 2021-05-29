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
