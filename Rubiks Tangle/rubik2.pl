:- use_module(library(clpfd)).
:- use_module(library(clpq)).
:- use_module(library(assoc)).
:- use_module(library(lists)).
:- bb_put(counter, 0).

%---- Generate Piece ----

genRubik(Piece):-
	Piece = [A,B,C,D,E,F,G,H],
	domain(Piece, 1, 4),
	(A #= D #/\ B #= F #/\ C #= H #/\ E #= G) #\/ (A #= F #/\ B #= G #/\ C #= E #/\ D #= H) #\/
	(A #= C #/\ B #= F #/\ D #= G #/\ E #= H) #\/ (A #= G #/\ B #= E #/\ D #= H #/\ C #= F),
	global_cardinality(Piece, [1-2, 2-2, 3-2,4-2]).
	
/*constraintSeq([A,B,C,D,E,F,G,H|_], [I,J,K,L,M,N,O,P], 1):-
	A #= I #/\ B #= J #/\ C #= K #/\ D #= L #/\ E #= M #/\ F #= N #/\ G #= O #/\ H #= P.*/
	
constraintSeq([_,_,_,_,_,_,_,_|T], [I,J,K,L,M,N,O,P], Aux, 1, Final):-
	append(Aux, [I,J,K,L,M,N,O,P], Aux2),
	append(Aux2, T, Final).
	
constraintSeq([A,B,C,D,E,F,G,H|T], Square, Aux, Pos, Final):-
	Pos > 1,
	New is Pos-1,
	append(Aux, [A,B,C,D,E,F,G,H], NewAux),
	constraintSeq(T, Square, NewAux, New, Final).
	
getFromSeq([A,B,C,D,E,F,G,H|_], [A,B,C,D,E,F,G,H], 1).
	
getFromSeq([_,_,_,_,_,_,_,_|T], Square, Pos):-
	Pos > 1,
	New is Pos-1,
	getFromSeq(T,Square,New).

%---- Draw Table ----

/*
Consider all predicates that draw the table
*/

drawTopLine([]).

drawTopLine([[A,B,_,_,_,_,_,_]|T]) :-
	write(' '), write(B), write(A), write(' '), write('|'), drawTopLine(T).

drawMiddle1Line([]).

drawMiddle1Line([[_,_,C,_,_,_,_,H]|T]) :-
	write(C), write(' '), write(' '), write(H), write('|'), drawMiddle1Line(T).

drawMiddle2Line([]).

drawMiddle2Line([[_,_,_,D,_,_,G,_]|T]) :-
	write(D), write(' '), write(' '), write(G), write('|'),drawMiddle2Line(T).

drawBottomLine([]).

drawBottomLine([[_,_,_,_,E,F,_,_]|T]) :-
	write(' '), write(E), write(F), write(' '), write('|'), drawBottomLine(T).

drawLineDiv([]):- 
	write('+').

drawLineDiv([[_,_,_,_,_,_,_,_]|T]):-
	write('+----'),drawLineDiv(T).

drawLine(Line):-
	write('|'),drawTopLine(Line),nl,
	write('|'),drawMiddle1Line(Line),nl,
	write('|'),drawMiddle2Line(Line),nl,
	write('|'),drawBottomLine(Line),nl.

drawBoard([Line|[]]):-
	drawLineDiv(Line),nl,
	drawLine(Line),
	drawLineDiv(Line),nl.

drawBoard([Line|T]):-
	drawLineDiv(Line),nl,
	drawLine(Line),drawBoard(T).

%---- List Predicates ----

/*
Consider all the predicates that work with Lists all around the program
*/

%- Gets a certain position in the sequence to label -

getPosInSequence([H|_], 1, Result):-
	Result #= H.
	
getPosInSequence([_|T], Pos, Result):-
	Pos #> 1,
	NewPos #= Pos-1,
	getPosInSequence(T, NewPos, Result).
	
%- Gets a list of lists that will have the correct format for the final result-
	
getListOfLists([], _, _, Int, Int).
	
getListOfLists([H|T], Counter, Aux, Int, Final):-
	bb_get(dim, Dimension),
	length(Int, X),
	(X < Dimension ->
		(Counter < Dimension ->
			New is Counter+1,
			getSquareInListNoConstraint(H, ResultingSquare),
			append(Aux, [ResultingSquare], Aux2),
			getListOfLists(T, New, Aux2, Int, Final);
			(Counter = Dimension ->
				New is Counter+1,
				getSquareInListNoConstraint(H, ResultingSquare),
				append(Aux, [ResultingSquare], Aux2),
				append(Int, [Aux2], ResultingIntermediate),
				getListOfLists(T, 1, [], ResultingIntermediate, Final)))).
				
getListOfListsNormal([], _, _, Int, Int).
	
getListOfListsNormal([A,B,C,D,E,F,G,H|T], Counter, Aux, Int, Final):-
	bb_get(dim, Dimension),
	length(Int, X),
	(X < Dimension ->
		(Counter < Dimension ->
			New is Counter+1,
			append(Aux, [[A,B,C,D,E,F,G,H]], Aux2),
			getListOfListsNormal(T, New, Aux2, Int, Final);
			(Counter = Dimension ->
				New is Counter+1,
				append(Aux, [[A,B,C,D,E,F,G,H]], Aux2),
				append(Int, [Aux2], ResultingIntermediate),
				getListOfListsNormal(T, 1, [], ResultingIntermediate, Final)))).

%---- Rotate ----

/*
Consider all the predicates that work with square Rotations
*/

rotateSquare([A,B,C,D,E,F,G,H], Final):-
	append([C,D,E,F,G,H], [A,B], Final).
	
rotateSquareNum(Square, 0, Square).

rotateSquareNum(Square, NumTimes, Resulting):- %Rotates a Square NumTimes
	NumTimes #> 0 #/\ NumTimes #< 4,
	NewTimes #= NumTimes-1,
	rotateSquare(Square, NewSquare),
	rotateSquareNum(NewSquare, NewTimes, Resulting).
	
rotateSquareNumNoConstraint(Square, 0, Square).
	
rotateSquareNumNoConstraint(Square, NumTimes, Resulting):- %Rotates a Square NumTimes with no Constraints (Used to rotate Squares after labeling's done)
	NewTimes is NumTimes-1,
	rotateSquare(Square, NewSquare),
	rotateSquareNum(NewSquare, NewTimes, Resulting).
	
	
%---- Main Constraint Rule ----

constrainInRubik([H|_], 1, Square):-
	H #= Square / 4.
	
constrainInRubik([_|T], Pos, Square):-
	New is Pos-1,
	constrainInRubik(T, New, Square).
	
%---- Square Operations ----

/*
Consider all predicates that are used for Square Operations
*/
			
getSquare([A,B,C,D,E,F,G,H|_], 0, [A,B,C,D,E,F,G,H] ).

getSquare([_,_,_,_,_,_,_,_|T], Pos, Square):- %Rotates a Square NumTimes
	Pos #> 0,
	NewPos #= Pos-1,
	getSquare(T, NewPos, Square).
			
getSquareNoConstraint([A,B,C,D,E,F,G,H|_], 0, [A,B,C,D,E,F,G,H] ).
			
getSquareNoConstraint([_,_,_,_,_,_,_,_|T], Pos, Square):- %Rotates a Square NumTimes with no Constraints (Used to get Squares after labeling's done)
	NewPos is Pos-1,
	getSquare(T, NewPos, Square).
	
getRight([_ ,_ ,_ ,_ ,_ ,_, G, H],G, H). %Gets the right bounds (colors) of the square
getLeft([_, _, C, D, _, _, _, _], C, D). %Gets the left bounds (colors) of the square
getUp([A, B, _, _, _, _, _, _], A, B). %Gets the upper bounds (colors) of the square
getDown([_, _, _, _, E, F, _, _], E, F ). %Gets the bottom bounds (colors) of the square
	
constraintVerticalDown(S1,S2):- %Constrains two squares vertically, comparing the current one with the one under it
	getDown(S1, D1, D2),
	getUp(S2, D3, D4),
	D1 #= D4 #/\ D2 #= D3.
	
constraintVerticalUp(S1,S2):- %Constrains two squares vertically, comparing the current one with the one above it
	getDown(S2, D1, D2),
	getUp(S1, D3, D4),
	D1 #= D4 #/\ D2 #= D3.
	
constraintHorizontalLeft(S1,S2):- %Constrains two squares horizontally, comparing the current one with the one to its left
	getLeft(S1, D1, D2),
	getRight(S2, D3, D4),
	D1 #= D4 #/\ D2 #= D3.
	
constraintHorizontalRight(S1,S2):- %Constrains two squares horizontally, comparing the current one with the one to its right
	getLeft(S2, D1, D2),
	getRight(S1, D3, D4),
	D1 #= D4 #/\ D2 #= D3.

repetitive(Repeated, Inner,Piece):- %Gets a square from the repeated list
	NewPiece #= Piece-24, 
	getSquare(Repeated, NewPiece, Inner).

getSquareInList(Sequence, Pos, ResultingSquare, Rubik):- %Gets a square from the Square List
	bb_get(squares, Squares),
	bb_get(repSquares, Repeated),
	getPosInSequence(Sequence, Pos, Square),
	constrainInRubik(Rubik, Pos, Square),
	Piece #= Square / 4  #/\ Rot #= Square mod 4,
	(Piece #=< 23 -> 
		getSquare(Squares, Piece, Inner), rotateSquareNum(Inner, Rot, ResultingSquare); 
		(Piece #> 23 -> 
			repetitive(Repeated, Inner, Piece), rotateSquareNum(Inner, Rot, ResultingSquare))).
			

getSquareInListNoConstraint(Square, ResultingSquare):- %Gets a square from the Square List with no Constraints (used after labelling's done)
	bb_get(squares, Squares),
	bb_get(repSquares, Repeated),
	Piece is Square // 4,
	Rot is Square mod 4,
	(Piece =< 23 -> 
		getSquareNoConstraint(Squares, Piece, Inner), rotateSquareNumNoConstraint(Inner, Rot, ResultingSquare); 
		(Piece > 23 -> 
			repetitive(Repeated, Inner, Piece), rotateSquareNumNoConstraint(Inner, Rot, ResultingSquare))).

%---- Verifying The Squares ----

/*
Consider all predicates that compare squares with their adjacents
*/

verifyDownAndRight(Sequence, Central, Right, Under, Rubik, Final):- %Verifies all squares that are under it and to its right
	bb_get(gen, Gen),
	bb_get(line, L),
	(Gen = 0 ->
		!, getSquareInList(Sequence, Central, ResA, Rubik),
		getSquareInList(Sequence, Right, ResB, Rubik),
		constraintHorizontalRight(ResA,ResB),
		getSquareInList(Sequence, Under, ResC, Rubik),
		constraintVerticalDown(ResA,ResC);
		(L = 1 ->
			(Central = 1 ->
				!, genRubik(S1), !, genRubik(S2), !,
				constraintHorizontalRight(S1,S2),
				labeling([ffc], S2),
				!, genRubik(S3), !,
				constraintVerticalDown(S1,S3),
				labeling([ffc], S1), 
				labeling([ffc], S3),
				constraintSeq(Sequence, S1, [], Central, Int1),
				constraintSeq(Int1, S3, [], Under, Int2),
				constraintSeq(Int2, S2, [], Right, Final);
				getFromSeq(Sequence, S1, Central), !, genRubik(S2), 
				constraintHorizontalRight(S1,S2),
				labeling([ffc], S2),
				!, genRubik(S3), !,
				constraintVerticalDown(S1,S3),
				labeling([ffc], S3),
				constraintSeq(Sequence, S3, [], Under, Int2),
				constraintSeq(Int2, S2, [], Right, Final));
				getFromSeq(Sequence, S1, Central), getFromSeq(Sequence, S2, Right), 
				constraintHorizontalRight(S1,S2),
				!, genRubik(S3), !,
				constraintVerticalDown(S1,S3),
				labeling([ffc], S3),
				constraintSeq(Sequence, S3, [], Under, Final))).
		
verifyRight(Sequence, Central, Right, Above, Final):-
	NewAbove is Above+1,
	getFromSeq(Sequence, S1, Central), getFromSeq(Sequence, S3, NewAbove), !, genRubik(S2), !,
	constraintHorizontalRight(S1,S2),
	constraintVerticalUp(S2,S3),
	labeling([ffc], S2),
	constraintSeq(Sequence, S2, [], Right, Final).
	
verifyDown(Sequence, Central, Under, Final):-
	getFromSeq(Sequence, S1, Central), !, genRubik(S2), !,
	constraintVerticalDown(S1,S2),
	labeling([ffc], S2),
	constraintSeq(Sequence, S2, [], Under, Final).

verifyLeftRightUnder(Sequence, Central, Right, Left, Under, Rubik):- %Verifies all squares that are under it, to its right and to its left
	bb_get(gen, Gen),
	(Gen = 0 ->
		!, getSquareInList(Sequence, Central, ResA, Rubik),
		getSquareInList(Sequence, Left, ResB, Rubik),
		constraintHorizontalLeft(ResA,ResB),
		getSquareInList(Sequence, Right, ResC, Rubik),
		constraintHorizontalRight(ResA,ResC),
		getSquareInList(Sequence, Under, ResD, Rubik),
		constraintVerticalDown(ResA, ResD)).		
	
verifyLeftUnder(Sequence, Central, Left, Under, Rubik):- %Verifies all squares that are under it and to its left
	bb_get(gen, Gen),
	(Gen = 0 ->
		getSquareInList(Sequence, Central, ResA, Rubik),
		getSquareInList(Sequence, Left, ResB, Rubik),
		constraintHorizontalLeft(ResA,ResB),
		getSquareInList(Sequence, Under, ResC, Rubik),     
		constraintVerticalDown(ResA,ResC)).
	
verifyDownUpRight(Sequence, Central, Right, Under, Above, Rubik):- %Verifies all squares that are under it, above it and to its right
	bb_get(gen, Gen),
	(Gen = 0 ->	
		!, getSquareInList(Sequence, Central, ResA, Rubik),
		getSquareInList(Sequence, Right, ResB, Rubik),
		constraintHorizontalRight(ResA,ResB),
		getSquareInList(Sequence, Under, ResC, Rubik),
		constraintVerticalDown(ResA,ResC),
		getSquareInList(Sequence, Above, ResD, Rubik),
		constraintVerticalUp(ResA,ResD)).	
	
verifyAllDirections(Sequence, Central, Right, Left, Under, Above, Rubik):- %Verifies all squares that are under it, above it, to its left and to its right
	bb_get(gen, Gen),
	(Gen = 0 ->	
		!, getSquareInList(Sequence, Central, ResA, Rubik),
		getSquareInList(Sequence, Left, ResB, Rubik),
		constraintHorizontalLeft(ResA,ResB),
		getSquareInList(Sequence, Right, ResC, Rubik),
		constraintHorizontalRight(ResA,ResC),
		getSquareInList(Sequence, Under, ResD, Rubik),
		constraintVerticalDown(ResA,ResD),
		getSquareInList(Sequence, Above, ResE, Rubik),
		constraintVerticalUp(ResA,ResE)).		
	
verifyLeftUnderAbove(Sequence, Central, Left, Under, Above, Rubik):- %Verifies all squares that are under it, above it and to its left
	bb_get(gen, Gen),
	(Gen = 0 ->	
		!, getSquareInList(Sequence, Central, ResA, Rubik),
		getSquareInList(Sequence, Left, ResB, Rubik),
		constraintHorizontalLeft(ResA,ResB),
		getSquareInList(Sequence, Under, ResC, Rubik),
		constraintVerticalDown(ResA,ResC),
		getSquareInList(Sequence, Above, ResD, Rubik),
		constraintVerticalUp(ResA,ResD)).			
	
verifyRightAbove(Sequence, Central, Right, Above, Rubik):- %Verifies all squares that are above it and to its right
	bb_get(gen, Gen),
	(Gen = 0 ->	
		!, getSquareInList(Sequence, Central, ResA, Rubik),
		getSquareInList(Sequence, Right, ResB, Rubik),
		constraintHorizontalRight(ResA,ResB),
		getSquareInList(Sequence, Above, ResC, Rubik),       
		constraintVerticalUp(ResA,ResC)).		
	
verifyLeftAbove(Sequence, Central, Left, Above, Rubik):- %Verifies all squares that are above it and to its left
	bb_get(gen, Gen),
	(Gen = 0 ->	
		!, getSquareInList(Sequence, Central, ResA, Rubik),
		getSquareInList(Sequence, Left, ResB, Rubik),
		constraintHorizontalLeft(ResA,ResB),
		getSquareInList(Sequence, Above, ResC, Rubik),
		constraintVerticalUp(ResA,ResC)).
	
verifyLeftRightAbove(Sequence, Central, Right, Left, Above, Rubik):- %Verifies all squares that are above it, to its left and to its right
	bb_get(gen, Gen),
	(Gen = 0 ->	
		!, getSquareInList(Sequence, Central, ResA, Rubik),
		getSquareInList(Sequence, Left, ResB, Rubik),
		constraintHorizontalLeft(ResA,ResB),
		getSquareInList(Sequence, Right, ResC, Rubik),
		constraintHorizontalRight(ResA,ResC),
		getSquareInList(Sequence, Above, ResD, Rubik),
		constraintVerticalUp(ResA,ResD)).
	
%-----First Line----

/*
Consider all predicates that take care of the first line
*/

constraintAllNormalLine1(Sequence, Dimension, 1, Size, Rubik, FinalS):- %Verifies first element of the first Line
	bb_put(line, 1),
	!,
	Under is 1+Dimension,
	bb_get(gen, Gen),
	(Gen = 0 ->
		verifyDownAndRight(Sequence, 1, 2, Under, Rubik, Final),
		constraintAllNormal(Sequence, Size, 2, 1, Rubik, FinalS);
		verifyDownAndRight(Sequence, 1, 2, Under, Rubik, Final),
		constraintAllNormal(Final, Size, 2, 1, Rubik, FinalS)).

constraintAllNormalLine1(Sequence, Dimension, Current, Size, Rubik, FinalS):- %Verifies the remaining elements of the first Line
	bb_put(line, 1),
	!,
	(Current < Dimension ->
		LeftOfCurrent is Current-1,
		RightOfCurrent is Current+1,
		UnderOfCurrent is Current+Dimension,
		New is Current+1,
		bb_get(gen, Gen),
		(Gen = 0 ->
			verifyLeftRightUnder(Sequence, Current, RightOfCurrent, LeftOfCurrent, UnderOfCurrent, Rubik),
			constraintAllNormal(Sequence, Size, New, 1, Rubik, FinalS);
			verifyDownAndRight(Sequence, Current, RightOfCurrent, UnderOfCurrent, Rubik, Final),
			constraintAllNormal(Final, Size, New, 1, Rubik, FinalS));
		(Current = Dimension ->
			LeftOfCurrent is Current-1,
			UnderOfCurrent is Current+Dimension,
			bb_get(gen, Gen),
			(Gen = 0 ->
				verifyLeftUnder(Sequence, Current, LeftOfCurrent, UnderOfCurrent, Rubik),
				constraintAllNormal(Sequence, Size, 1, 2, Rubik, FinalS);
				verifyDown(Sequence, Current, UnderOfCurrent, Final),
				constraintAllNormal(Final, Size, 1, 2, Rubik, FinalS)))).
			
%---- Any Line ----

/*
Consider all predicates that take care of any line
*/
	
constraintAllNormalAnyLine(Sequence, Dimension, 1, FirstInLine, Size, Line, Rubik, FinalS):- %Verifies first element of the Current Line
	bb_put(line, Line),
	!,
	RightOfFirst is FirstInLine+1,
	Under is FirstInLine+Dimension,
	Above is FirstInLine-Dimension,
	bb_get(gen, Gen),
	(Gen = 0 ->
		verifyDownUpRight(Sequence, FirstInLine, RightOfFirst, Under, Above, Rubik),
		constraintAllNormal(Sequence, Size, 2, Line, Rubik, FinalS);
		verifyDownAndRight(Sequence, FirstInLine, RightOfFirst, Under, Rubik, Final),
		constraintAllNormal(Final, Size, 2, Line, Rubik, FinalS)).
	
constraintAllNormalAnyLine(Sequence, Dimension, Current, FirstInLine, Size, Line, Rubik, FinalS):- %Verifies the reminaing elements of the Current Line
	bb_put(line, Line),
	!,
	(Current < Dimension -> 
		CurrentSquare is FirstInLine+(Current-1),
		LeftOfCurrent is CurrentSquare-1,
		RightOfCurrent is CurrentSquare+1,
		UnderOfCurrent is CurrentSquare+Dimension,
		AboveOfCurrent is CurrentSquare-Dimension,
		New is Current+1,
		bb_get(gen, Gen),
		(Gen = 0 ->
			verifyAllDirections(Sequence, CurrentSquare, RightOfCurrent, LeftOfCurrent, UnderOfCurrent, AboveOfCurrent, Rubik),
			constraintAllNormal(Sequence, Size, New, Line, Rubik, FinalS);
			verifyDownAndRight(Sequence, CurrentSquare, RightOfCurrent, UnderOfCurrent, Rubik, Final),
			constraintAllNormal(Final, Size, New, Line, Rubik, FinalS));
		(Current = Dimension ->
			CurrentSquare is FirstInLine+(Current-1),
			LeftOfCurrent is CurrentSquare-1,
			UnderOfCurrent is CurrentSquare+Dimension,
			AboveOfCurrent is CurrentSquare-Dimension,
			NewLine is Line+1,
			bb_get(gen, Gen),
			(Gen = 0 ->
				verifyLeftUnderAbove(Sequence, CurrentSquare, LeftOfCurrent, UnderOfCurrent, AboveOfCurrent, Rubik),
				constraintAllNormal(Sequence, Size, 1, NewLine, Rubik, FinalS);
				verifyDown(Sequence, CurrentSquare, UnderOfCurrent, Final),
				constraintAllNormal(Final, Size, 1, NewLine, Rubik, FinalS)))).		
	
%---- Last Line ----

/*
Consider all predicates that take care of the last line
*/
	
constraintAllNormalLastLine(Sequence, Dimension, 1, FirstInLine, Size, Rubik, FinalS):- %Verifies first element of the Last Line
	!,
	RightOfFirst is FirstInLine+1,
	Above is FirstInLine-Dimension,
	bb_get(gen, Gen),
	(Gen = 0 ->	
		verifyRightAbove(Sequence, FirstInLine, RightOfFirst, Above, Rubik),
		constraintAllNormal(Sequence, Size, 2, Dimension, Rubik, FinalS);
		verifyRight(Sequence, FirstInLine, RightOfFirst, Above, Final),
		constraintAllNormal(Final, Size, 2, Dimension, Rubik, FinalS)).
	
constraintAllNormalLastLine(Sequence, Dimension, Current, FirstInLine, Size, Rubik, FinalS):- %Verifies the reminaing elements of the Last Line
	!,
	(Current < Dimension -> 
		CurrentSquare is FirstInLine+(Current-1),
		LeftOfCurrent is CurrentSquare-1,
		RightOfCurrent is CurrentSquare+1,
		AboveOfCurrent is CurrentSquare-Dimension,
		New is Current+1,
		bb_get(gen, Gen),
		(Gen = 0 ->	
			verifyLeftRightAbove(Sequence, CurrentSquare, RightOfCurrent, LeftOfCurrent, AboveOfCurrent, Rubik),
			constraintAllNormal(Sequence, Size, New, Dimension, Rubik, FinalS);
			verifyRight(Sequence, CurrentSquare, RightOfCurrent, AboveOfCurrent, Final),
			constraintAllNormal(Final, Size, New, Dimension, Rubik, FinalS));
			(Current = Dimension ->
				CurrentSquare is FirstInLine+(Current-1),
				RightOfCurrent is CurrentSquare-1,
				AboveOfCurrent is CurrentSquare-Dimension,
				bb_get(gen, Gen),
				(Gen = 0 ->
					verifyLeftAbove(Sequence, CurrentSquare, RightOfCurrent, AboveOfCurrent, Rubik);
					FinalS = Sequence))).
	
%---- Sequence Check ----

/*
Consider the predicates that use the auxiliar predicates to check elements in the Sequence
*/

constraintAllNormal(Sequence, Size, 1, 1, Rubik, FinalS):-
	bb_get(dim, Dimension),
	constraintAllNormalLine1(Sequence, Dimension, 1, Size, Rubik, FinalS).
	
constraintAllNormal(Sequence, Size, Current, 1, Rubik, FinalS):-
	bb_get(dim, Dimension),
	constraintAllNormalLine1(Sequence, Dimension, Current, Size, Rubik, FinalS).

constraintAllNormal(Sequence, Size, 1, Line, Rubik, FinalS):-
	bb_get(dim, Dimension),
	FirstInLine is Dimension * (Line-1)+1,
	constraintAllNormalAnyLine(Sequence, Dimension, 1, FirstInLine, Size, Line, Rubik, FinalS).
	
constraintAllNormal(Sequence, Size, Current, Line, Rubik, FinalS):-
	bb_get(dim, Dimension),
	FirstInLine is Dimension * (Line-1)+1,
	bb_put(line, Line),
	(Line < Dimension ->
		constraintAllNormalAnyLine(Sequence, Dimension, Current, FirstInLine, Size, Line, Rubik, FinalS);
		(Line = Dimension -> constraintAllNormalLastLine(Sequence, Dimension, Current, FirstInLine, Size, Rubik, FinalS))).

constraintFinal(_, [], _).
		
constraintFinal(Sequence, [H|T], Counter):-
	New is Counter+1,
	getPosInSequence(Sequence, Counter, Pos),
	H #= Pos / 4,
	constraintFinal(Sequence, T, New).
	
printSequence([]):- nl.
	
printSequence([H|T]):-
	Piece is H//4,
	Rot is H mod 4,
	write(Piece), write(' - '), write(Rot), nl,
	printSequence(T).
	
	
		
%---- Rubik Sequences ----

rubikMini:-
	MiniSquare = [2,1,4,2,3,1,3,4,
					3,1,4,3,2,1,2,4,
					3,2,4,3,1,2,1,4,
					2,4,1,2,3,4,3,1,
					2,4,3,2,1,4,1,3,
					1,3,2,1,4,3,4,2,
					3,1,2,3,4,1,4,2,
					1,4,2,1,3,4,3,2,
					4,2,3,4,1,2,1,3],
	bb_put(gen, 0),					
	bb_put(dim, 3),
	bb_put(squares, MiniSquare),
	bb_put(repSquares, []),
	Size is 3 * 3,
	length(Rubik, Size),
	domain(Rubik, 0,8),
	all_different(Rubik),
	length(Sequence, Size), %Cria lista com Size Elementos
	domain(Sequence, 0, 35),
	all_different(Sequence),
	constraintAllNormal(Sequence, Size, 1, 1, Rubik, []),
	labeling([ffc], Sequence),
	labeling([ffc], Rubik),
	nl, write(Rubik), nl,
	getListOfLists(Sequence, 1, [], [], Final),
	drawBoard(Final).
	
rubik4:-
	Squares = [2,1,4,2,3,1,3,4,
				4,2,3,4,1,2,1,3,
				1,3,2,1,4,3,4,2,
				4,2,1,4,3,2,3,1,
				2,3,1,2,4,3,4,1,
				2,4,3,2,1,4,1,3,
				3,1,4,3,2,1,2,4,
				2,4,1,2,3,4,3,1,
				4,3,2,4,1,3,1,2,
				1,2,4,1,3,2,3,4,
				3,1,2,3,4,1,4,2,
				1,4,2,1,3,4,3,2,        
				3,2,4,3,1,2,1,4,
				4,1,3,4,2,1,2,3,
				4,3,1,4,2,3,2,1,
				3,4,1,3,2,4,2,1,
				2,1,3,2,4,1,4,3,
				1,3,4,1,2,3,2,4,
				1,4,3,1,2,4,2,3,
				3,2,1,3,4,2,4,1,
				3,4,2,3,1,4,1,2,
				1,2,3,1,4,2,4,3,
				2,3,4,2,1,3,1,4,
				4,1,2,4,3,1,3,2],
	bb_put(gen, 0),						
	bb_put(dim, 4),
	bb_put(squares, Squares),
	bb_put(repSquares, []),
	Size is 4 * 4,
	length(Rubik, Size),
	domain(Rubik, 0, 23),
	all_different(Rubik),
	length(Sequence, Size),
	domain(Sequence, 0, 95),
	all_different(Sequence),
	constraintAllNormal(Sequence, Size, 1, 1, Rubik, []),
	constraintFinal(Sequence, Rubik, 1),
	labeling([ffc], Sequence),
	labeling([ffc], Rubik),
	nl, write(Rubik), nl,
	getListOfLists(Sequence, 1, [], [], Final),
	drawBoard(Final).
	
rubik5:-
	Squares = [2,1,4,2,3,1,3,4,
				4,2,3,4,1,2,1,3,
				1,3,2,1,4,3,4,2,
				4,2,1,4,3,2,3,1,
				2,3,1,2,4,3,4,1,
				2,4,3,2,1,4,1,3,
				3,1,4,3,2,1,2,4,
				2,4,1,2,3,4,3,1,
				4,3,2,4,1,3,1,2,
				1,2,4,1,3,2,3,4,
				3,1,2,3,4,1,4,2,
				1,4,2,1,3,4,3,2,        
				3,2,4,3,1,2,1,4,
				4,1,3,4,2,1,2,3,
				4,3,1,4,2,3,2,1,
				3,4,1,3,2,4,2,1,
				2,1,3,2,4,1,4,3,
				1,3,4,1,2,3,2,4,
				1,4,3,1,2,4,2,3,
				3,2,1,3,4,2,4,1,
				3,4,2,3,1,4,1,2,
				1,2,3,1,4,2,4,3,
				2,3,4,2,1,3,1,4,
				4,1,2,4,3,1,3,2],
				
	RepeatedSquares = [1,2,4,1,3,2,3,4,
						3,1,2,3,4,1,4,2,
						4,1,3,4,2,1,2,3,
						2,3,4,2,1,3,1,4],
	bb_put(gen, 0),						
	bb_put(dim, 5),
	bb_put(squares, Squares),
	bb_put(repSquares, RepeatedSquares),
	Size is 5 * 5,
	length(Rubik, Size),
	domain(Rubik, 0, 27),
	all_different(Rubik),
	length(Sequence, Size),
	domain(Sequence, 0, 111),
	all_different(Sequence),
	constraintAllNormal(Sequence, Size, 1, 1, Rubik, []),
	labeling([ff], Sequence),
	labeling([ff], Rubik),
	nl, write(Rubik), nl,
	getListOfLists(Sequence, 1, [], [], Final),
	drawBoard(Final).

rubik2:-
	MiniSquare = [2,1,4,2,3,1,3,4,
					3,1,4,3,2,1,2,4,
					3,2,4,3,1,2,1,4,
					2,4,1,2,3,4,3,1,
					2,4,3,2,1,4,1,3,
					1,3,2,1,4,3,4,2,
					3,1,2,3,4,1,4,2,
					1,4,2,1,3,4,3,2,
					4,2,3,4,1,2,1,3],
	bb_put(gen, 0),	
	bb_put(squares, MiniSquare),
	bb_put(repSquares, []),	
	bb_put(dim, 2),
	Size is 2 * 2,
	length(Rubik, Size),
	domain(Rubik, 0, 8),
	all_different(Rubik),
	length(Sequence, Size),
	domain(Sequence, 0, 35),
	all_different(Sequence),
	constraintAllNormal(Sequence, Size, 1, 1, Rubik, []),
	labeling([ff], Sequence),
	labeling([ff], Rubik),
	nl, write(Rubik), nl,
	getListOfLists(Sequence, 1, [], [], Final),
	drawBoard(Final).
	
rubikFlex(Dim):-
	Squares = [2,1,4,2,3,1,3,4,
				4,2,3,4,1,2,1,3,
				1,3,2,1,4,3,4,2,
				4,2,1,4,3,2,3,1,
				2,3,1,2,4,3,4,1,
				2,4,3,2,1,4,1,3,
				3,1,4,3,2,1,2,4,
				2,4,1,2,3,4,3,1,
				4,3,2,4,1,3,1,2,
				1,2,4,1,3,2,3,4,
				3,1,2,3,4,1,4,2,
				1,4,2,1,3,4,3,2,        
				3,2,4,3,1,2,1,4,
				4,1,3,4,2,1,2,3,
				4,3,1,4,2,3,2,1,
				3,4,1,3,2,4,2,1,
				2,1,3,2,4,1,4,3,
				1,3,4,1,2,3,2,4,
				1,4,3,1,2,4,2,3,
				3,2,1,3,4,2,4,1,
				3,4,2,3,1,4,1,2,
				1,2,3,1,4,2,4,3,
				2,3,4,2,1,3,1,4,
				4,1,2,4,3,1,3,2],
				
	RepeatedSquares = [1,2,4,1,3,2,3,4,
						3,1,2,3,4,1,4,2,
						4,1,3,4,2,1,2,3,
						2,3,4,2,1,3,1,4],
	bb_put(gen, 0),	
	bb_put(squares, Squares),
	bb_put(repSquares, RepeatedSquares),
	bb_put(dim, Dim),
	Size is Dim * Dim,
	MaxDomain is Dim-1,
	length(Rubik, Size),
	domain(Rubik, 0, MaxDomain),
	length(Sequence, Size),
	domain(Sequence, 0, 111),
	all_different(Sequence),
	constraintAllNormal(Sequence, Size, 1, 1, Rubik),
	labeling([ff], Sequence),
	labeling([ff], Rubik),
	nl, write(Rubik), nl,
	getListOfLists(Sequence, 1, [], [], Final),
	drawBoard(Final).
	
flexible:-
	nl,nl,
	write('Size to Tangle (between 6 and 9): '),
	read(Size),
	write('Beware, the following operation might take too long!'), nl, nl,
	rubikFlex(Size).
			
genFlex(Dim):-
	bb_put(dim, Dim),
	bb_put(gen, 1),	
	Size is Dim*Dim,
	SeqLimit is 8*Size,
	length(Sequence, SeqLimit),
	constraintAllNormal(Sequence, Size, 1, 1, [], Final),
	getListOfListsNormal(Final, 1, [], [], FinalS),
	drawBoard(FinalS).
	
genFlexible:-
	nl,nl,
	write('Size to Tangle (any): '),
	read(Size),
	write('Beware, the following operation might take too long!'), nl, nl,
	genFlex(Size).
	
%---- Main Part of the Program ----
	
rubik:-
	write('+------- Rubik큦 Tangle -------+'), nl,
	write('| 1 - 2x2 Rubik큦 Tangle       |'), nl,
	write('| 2 - 3x3 Rubik큦 Tangle(Mini) |'), nl,
	write('| 3 - 4x4 Rubik큦 Tangle       |'), nl,
	write('| 4 - 5x5 Rubik큦 Tangle       |'), nl,
	write('| 5 - Flexible Rubik큦 Tangle  |'), nl,
	write('| 6 - Generate Rubik큦 Tangle  |'), nl,
	write('|------------------------------|'), nl,
	write('Option: '),
	read(Option),
	(Option = 1 -> rubik2;
		(Option = 2 -> rubikMini;
			(Option = 3 -> rubik4;
				(Option = 4 -> rubik5;
					(Option = 5 -> flexible;
						(Option = 6 -> genFlexible)))))),
	fd_statistics, nl.