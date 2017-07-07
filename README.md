# Sudoku Solver

Sudoku Solver based on [A Pencil-and-Paper
Algorithm for Solving Sudoku Puzzles](http://www.ams.org/notices/200904/tx090400460p.pdf), written in Java.

### Usage

* Create a file with the contents of the unsolved sudoku.
* For cells which do not have a value, put a Zero (0).
* Following is an example of an unsolved sudoku file.
``` 
1,0,0,8,0,7,5,0,0
0,5,0,6,0,0,9,7,0
0,6,0,0,4,0,0,0,0
0,0,2,0,0,0,0,6,0
6,0,0,4,0,3,0,0,0
0,4,0,0,0,0,1,0,0
0,0,0,0,1,0,0,5,0
0,1,3,0,0,6,0,9,0
0,0,7,5,0,4,0,0,1 
```

Run sudoku solver with the unsolved sudoku file as an argument.
```
cd target
java -jar iSudoku-1.jar <path_to_unsolved_sudoku_file>
```