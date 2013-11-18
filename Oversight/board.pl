:-use_module(library(random)).
:-use_module(library(sockets)).

%-----------Sockets---------

% launch me in sockets mode

server(Socket, Stream, Port):-
	socket_server_open(Port, Socket),
	socket_server_accept(Socket, _Client, Stream, [type(text)]).

writeToSocket(Stream, Answer):-
	format(Stream, '~q.~n', [Answer]),
	flush_output(Stream).
	
readFromSocket(Stream, ReadMessage):-
	read(Stream, ReadMessage).
	
% wait for commands
serverLoop(Stream) :-
	repeat,
	read(Stream, ClientMsg),
	write('Received: '), write(ClientMsg), nl,
	parse_input(ClientMsg, MyReply),
	format(Stream, '~q.~n', [MyReply]),
	write('Wrote: '), write(MyReply), nl,
	flush_output(Stream),
	(ClientMsg == quit; ClientMsg == end_of_file), !.

parse_input(comando(Arg1, Arg2), Answer) :-
	comando(Arg1, Arg2, Answer).
	
parse_input(quit, ok-bye) :- !.
		
comando(Arg1, Arg2, Answer) :-
	write(Arg1), nl, write(Arg2), nl,
	Answer = 5.

%--------Initial Board-------------

initialBoard(
[['O','O','O','O','O','O','O'],
['O','O','O','O','O','O','O'],
['O','O','O','O','O','O','O'],
['O','O','O','O','O','O','O'],
['O','O','O','O','O','O','O'],
['O','O','O','O','O','O','O'],
['O','O','O','O','O','O','O']]
).
	
%---------------Tabuleiro----------------------

printBoardElement(Element) :-
        (Element = 'O' -> write(' '); write(Element)),
        write('|').

printBoard([],_):-
        write(' - - - - - - -'), nl,
        write(' a b c d e f g').

printBoard([Head|Tail], C) :-
        write(' - - - - - - -'), nl,
        write('|'),
        printRow(Head),
        write(C), nl,
        NewC is C +1,
        printBoard(Tail, NewC).

printRow([]).

printRow([Head|Tail]) :-
        printBoardElement(Head),
        printRow(Tail).

%----- Search Board For Elements in a Row --------

verifyBoardCThree([], StartPos, EndPos, _, Result, _):-
	Result is 0, EndPos is StartPos.
 
verifyBoardCThree([H|T], StartPos, EndPos, Situation, Result, Piece):-
	(Situation = 1 ->
		verifyListForThree(H, 0, 1, 1, Piece, Res),
		(Res = 0 -> 
			New is StartPos +1,
			verifyBoardCThree(T, New, EndPos, Situation, Result, Piece);
			Result is Res, EndPos is StartPos)).
 
verifyListForThree([], Counter, StartPos, _, _, End):-
	(Counter = 3 -> End is StartPos-4; End is 0).
 
verifyListForThree([Head|Tail], Counter, StartPos, Situation, Piece, End):-
	(Situation = 1 ->
	(Counter < 3 ->
		(Head = Piece, Head \= 'O' ->
			NewCounter is Counter+1,
			NewPos is StartPos+1,
			verifyListForThree(Tail, NewCounter, NewPos, Situation, Piece, End);
			NewCounter is 0,
			NewPos is StartPos+1,
			verifyListForThree(Tail, NewCounter, NewPos, Situation, Piece, End));
		End is StartPos)).
		
verifyThreeInRow(Board, Piece, Size, EndPos, Res):-
	getRowsInBoard(Board, 1, [], Rows, Size),
	getColumnsInBoard(Board, 1, [], Columns, Size),
	append(Rows,Columns, BoardC),
	verifyBoardCThree(BoardC, 1, EndPos, 1, Res, Piece).
 
%------------Logic-------------

verifyListForVictory([], Counter, _, _, _, End, Player):-
	(Counter = 4 -> victoryMessage(Player), End is 1; End is 0).

verifyListForVictory([Head|Tail], Counter, StartPos, Situation, Piece, End, Player):-
	(Situation = 1 ->
		(Counter < 4 ->
			(Head = Piece ->
				NewCounter is Counter+1,
				NewPos is StartPos+1,
				verifyListForVictory(Tail, NewCounter, NewPos, Situation, Piece, End, Player);
				NewCounter is 0,
				NewPos is StartPos+1,
				verifyListForVictory(Tail, NewCounter, NewPos, Situation, Piece, End, Player));
			NewSituation is 2,
			verifyListForVictory(Tail, Counter, StartPos, NewSituation, Piece, End, Player));
		victoryMessage(Player), End is 1).

%----------- Get Combinations -------------

getDiagonalInBoard(Board, HPos, VPos, ListAux, Diagonal, Size, Start):-
	(Size > Start ->
		verifyElementInPos(Board, HPos, VPos, Element, 1, 1),
		%write(Element), nl,
		append(ListAux, [Element], L2),
		NewHPos is HPos+1,
		NewVPos is VPos+1,
		New is Start+1,
		getDiagonalInBoard(Board, NewHPos, NewVPos, L2, Diagonal, Size, New);
		verifyElementInPos(Board, HPos, VPos, Element, 1, 1),
		append(ListAux, [Element], Diagonal)).
		
getDiagonalInBoardInverse(Board, HPos, VPos, ListAux, Diagonal, Size, Start):-
	(Size > Start ->
		verifyElementInPos(Board, HPos, VPos, Element, 1, 1),
		%write(Element), nl,
		append(ListAux, [Element], L2),
		NewHPos is HPos+1,
		NewVPos is VPos-1,
		New is Start+1,
		getDiagonalInBoardInverse(Board, NewHPos, NewVPos, L2, Diagonal, Size, New);
		verifyElementInPos(Board, HPos, VPos, Element, 1, 1),
		append(ListAux, [Element], Diagonal)).		
		
getHorizontalDiagonals(Board, HPos, VPos, ListAux, Diagonals, Size):-
	(Size > 4 ->
		getDiagonalInBoard(Board, HPos, VPos, [], Diagonal, Size, 1),
		append(ListAux, [Diagonal], L2),
		NewHPos is HPos+1,
		NewSize is Size-1,
		getHorizontalDiagonals(Board, NewHPos, VPos, L2, Diagonals, NewSize);
		getDiagonalInBoard(Board, HPos, VPos, [], Diagonal, Size, 1),
		append(ListAux, [Diagonal], Diagonals)).
		
getVerticalDiagonals(Board, HPos, VPos, ListAux, Diagonals, Size):-
	(Size > 4 ->
		getDiagonalInBoard(Board, HPos, VPos, [], Diagonal, Size, 1),
		append(ListAux, [Diagonal], L2),
		NewVPos is VPos+1,
		NewSize is Size-1,
		getVerticalDiagonals(Board, HPos, NewVPos, L2, Diagonals, NewSize);
		getDiagonalInBoard(Board, HPos, VPos, [], Diagonal, Size, 1),
		append(ListAux, [Diagonal], Diagonals)).
		
getHorizontalDiagonalsInverse(Board, HPos, VPos, ListAux, Diagonals, Size):-
	(Size > 4 ->
		getDiagonalInBoardInverse(Board, HPos, VPos, [], Diagonal, Size, 1),
		append(ListAux, [Diagonal], L2),
		NewHPos is HPos+1,
		NewSize is Size-1,
		getHorizontalDiagonalsInverse(Board, NewHPos, VPos, L2, Diagonals, NewSize);
		getDiagonalInBoardInverse(Board, HPos, VPos, [], Diagonal, Size, 1),
		append(ListAux, [Diagonal], Diagonals)).
		
getVerticalDiagonalsInverse(Board, HPos, VPos, ListAux, Diagonals, Size):-
	(Size > 4 ->
		getDiagonalInBoardInverse(Board, HPos, VPos, [], Diagonal, Size, 1),
		append(ListAux, [Diagonal], L2),
		NewVPos is VPos-1,
		NewSize is Size-1,
		getVerticalDiagonalsInverse(Board, HPos, NewVPos, L2, Diagonals, NewSize);
		getDiagonalInBoardInverse(Board, HPos, VPos, [], Diagonal, Size, 1),
		append(ListAux, [Diagonal], Diagonals)).
		
getAllDiagonals(Board, Diagonals, Size):-
	getVerticalDiagonals(Board, 1,1, [], VDiagonals, Size),
	getHorizontalDiagonals(Board, 1,1, [], HDiagonals, Size),
	getVerticalDiagonalsInverse(Board, 1,7 , [], VDiagonalsI, Size),
	getHorizontalDiagonalsInverse(Board, 1,7 , [], HDiagonalsI, Size),
	append(VDiagonals, HDiagonals, Aux),
	append(VDiagonalsI, HDiagonalsI, Aux2),
	append(Aux, Aux2, Diagonals).
		
getRowsInBoard(Board, Pos, ListAux, Rows, Size):-
	(Size > Pos ->
		selectListInBoard(Board, Pos, 1, Row),
		append(ListAux, [Row], L2),
		New is Pos+1,
		getRowsInBoard(Board, New, L2, Rows, Size);
		selectListInBoard(Board, Pos, 1, Row),
		append(ListAux, [Row], Rows)).
		
getColumnsInBoard(Board, Pos, ListAux, Columns, Size):-
	(Size > Pos ->
		getBoardColumn(Board, Pos, [], Column, Size, 1),
		append(ListAux, [Column], L2),
		New is Pos+1,
		getColumnsInBoard(Board, New, L2, Columns, Size);
		getBoardColumn(Board, Pos, [], Column, Size, 1),
		append(ListAux, [Column], Columns)).
		
getAllBoardCombinations(Board, BoardCombinations, Size):-
	getRowsInBoard(Board, 1, [], Rows, Size),
	getColumnsInBoard(Board, 1, [], Columns, Size),
	getAllDiagonals(Board, Diagonals, Size),
	append(Rows, Columns, Aux),
	append(Aux, Diagonals, BoardCombinations).

%---- Verify Element in Position ----------

verifyElementInPos([], _, _, _, _, _).

verifyElementInPos([H|T], HPos, VPos, Element, StartH, StartV):-
        (HPos = StartH -> verifyRow(H, VPos, Element, StartV);
                NextH is StartH+1,
                verifyElementInPos(T, HPos, VPos, Element, NextH, StartV)).
        
verifyRow([], _, _, _).

verifyRow([H|T], VPos, Element, StartV):-
        (VPos = StartV -> Element = H;
                NextV is StartV+1,
                verifyRow(T, VPos, Element, NextV)).
				
%------ Given Element Search For it in the list ------

searchAdjacentInBoard([], _, _, _, FinalH, FinalV):-
	FinalH is 0, FinalV is 0.

searchAdjacentInBoard([H|T], Piece, HPos, VPos, FinalH, FinalV):-
	searchAdjacentInList(H, 1, Piece, FinalPos),
	(FinalPos = 0 ->
		NewHPos is HPos+1,
		searchAdjacentInBoard(T, Piece, NewHPos, VPos, FinalH, FinalV);
		FinalH is HPos, FinalV is FinalPos).
		
searchAdjacentInList([], _, _, FinalPos):-
	FinalPos is 0.

searchAdjacentInList([H|T], Pos, Piece, FinalPos):-
	(H = Piece ->
		FinalPos is Pos;
		NewPos is Pos+1,
		searchAdjacentInList(T, NewPos, Piece, FinalPos)).
				
%------- Check all Board For Victory -------

verifyBoard(Board, Size, Piece, Player, Res):-
	getAllBoardCombinations(Board, BoardC, Size),
	verifyBoardC(BoardC, 1, Result, Piece, Player),
	(Result = 0 -> Res is 0; Res is 1).
	
verifyBoardC([], Situation, Result, _, _):-
	(Situation = 1 -> Result is 0).
	
verifyBoardC([H|T], Situation, Result, Piece, Player):-
	(Situation = 1 ->
		verifyListForVictory(H, 0, 1, 1, Piece, Res, Player),
		(Res = 0 -> 
			verifyBoardC(T, Situation, Result, Piece, Player);
			Result is 1)).
	

%------- Play Piece in a certain Position -----------
                
playPieceInPos([], _, _, _, _, _).

playPieceInPos([H|T], HPos, VPos, Element, StartH, StartV, ListAux, FinalBoard):-
	(HPos = StartH ->
		substituteInListPlay(H, Element, VPos, 1, [], Final),
		append(ListAux, [Final], List),
		append(List, T, FinalBoard);
		NextH is StartH+1,
		append(ListAux, [H], List),
		playPieceInPos(T, HPos, VPos, Element, NextH, StartV, List, FinalBoard)).
	
substituteInListPlay([Head|Tail],Element,Pos,StartPos,ListAux,Final):-
	(Pos = StartPos, Head = 'O' ->
		append(ListAux,[Element],L3),
		append(L3,Tail,Final);
		NextPos is StartPos+1,
		append(ListAux, [Head], L2),
		substituteInListPlay(Tail, Element, Pos, NextPos, L2,Final)).

substituteInList([Head|Tail],Element,Pos,StartPos,ListAux,Final):-
	(Pos = StartPos ->
		append(ListAux,[Element],L3),
		append(L3,Tail,Final);
		NextPos is StartPos+1,
		append(ListAux, [Head], L2),
		substituteInList(Tail, Element, Pos, NextPos, L2,Final)).
		
putBackListInBoard([Head|Tail], Pos, Start, List, ListAux, FinalBoard):-
	(Pos = Start ->
		append(ListAux, [List], L2),
		append(L2, Tail, FinalBoard);
		Next is Start+1,
		append(ListAux,[Head], L2),
		putBackListInBoard(Tail, Pos, Next, List, L2, FinalBoard)).
		
%------- Shift Board Row or Column -----------
	
getBoardColumn([Head|Tail], HPos, ListAux, Return, Size, Start):-
	(Size > Start ->
		verifyRow(Head, HPos, Element, 1),
		append(ListAux, [Element], Argument),
		NewStart is Start+1,
		getBoardColumn(Tail, HPos, Argument, Return, Size, NewStart);
		(Size = Start ->
			verifyRow(Head, HPos, Element, 1),
			append(ListAux, [Element], Return))).
				
shiftColumnFinal(Board, Pos, HPos, ListSwapped, Final, Size):-
	(Pos < Size ->
		selectListInBoard(Board, Pos, 1, ReturnedList),
		verifyRow(ListSwapped, Pos, CurrentElement, 1),
		substituteInList(ReturnedList, CurrentElement, HPos, 1, [], NewRow),
		putBackListInBoard(Board, Pos, 1, NewRow,[], NewBoard),
		NewPos is Pos +1,
		shiftColumnFinal(NewBoard, NewPos, HPos, ListSwapped, Final, Size);
		(Pos = Size ->
			selectListInBoard(Board, Pos, 1, ReturnedList),
			verifyRow(ListSwapped, Pos, CurrentElement, 1),
			substituteInList(ReturnedList, CurrentElement, HPos, 1, [], NewRow),
			putBackListInBoard(Board, Pos, 1, NewRow,[], Final))).
			
shiftColumn(Board, Pos, Direction, Final):-
	getBoardColumn(Board, Pos, [], Column, 7, 1),
	shiftRow(Column, Direction, 1, [], Shifted, 7),
	shiftColumnFinal(Board, 1, Pos, Shifted, Final, 7).
			
shiftRow([Head|Tail], Direction, Pos, ListAux, Final, Size):-
	(Direction = 1 -> %esquerda
		append(Tail, [Head], Final);
		(Direction = 2 -> %direita
			(Pos = Size ->
				append([Head],ListAux,Final);
				NextPos is Pos+1,
				append(ListAux, [Head], L2),
				shiftRow(Tail, Direction, NextPos, L2,Final, Size)))). 
					
selectListInBoard([H|T], ListPos, Start, FinalList):-
	(Start = ListPos ->
		append(H, [], FinalList);
		Next is Start+1,
		selectListInBoard(T, ListPos, Next, FinalList)).
		
%------ AI Related Predicates ----

askDificulty(Option):-
	write('1 - Fácil'), nl,
	write('2 - Médio'), nl,
	write('3 - Difícil'), nl,
	write('Opcao: '),
	read(Option),
	nl, nl.
	
generateRandomOption(Option):-
	random(1, 4, Option).
	
generateRandomPosition(Option, Size):-
	NewSize is Size+1,
	random(1, NewSize, Option).
	
generateRandomDirection(Option):-
	random(1, 3, Option).
	
generateOptionDecision(Option, Board, Piece, Size, HPos, VPos):-
	verifyThreeInRow(Board, Piece, Size, EndPos, Res),
	SizePlus is Size+1,
	SizeDouble is Size*2,
	((EndPos >= 1, EndPos =< Size) ->
		HPos is Res, VPos is EndPos, Option is 3;
		((EndPos >= SizePlus, EndPos =< SizeDouble) ->
			HPos is EndPos-SizePlus, VPos is Res, Option is 2;
			Option is 1, HPos is 0, VPos is 0)).
			
generateRandomPlayingSpot(PosGiven, PosFinal, Size):-
	(PosGiven = 1 ->
		PosFinal is PosGiven+1;
		(PosGiven = Size ->
			PosFinal is PosGiven-1;
			random(1,3,Result),
			(Result = 1 -> PosFinal is PosGiven+1; PosFinal is PosGiven-1))).
	
playingDecisionEasyAI(Element, Board, FinalBoard):-
	write('Posicao Horizontal (número): '),
	generateRandomPosition(HPos, 7),
	write(HPos), write('.'), nl,
	write('Posicao Vertical (letra): '),
	generateRandomPosition(VPos, 7),
	write(VPos), write('.'), nl,
	playPieceInPos(Board, HPos, VPos, Element, 1,1, [], FinalBoard).
	
playingDecisionMediumAI(Element, Board, FinalBoard, FirstPlay, Size, Random):-
	(Random = 1 ->
		write('Posicao Horizontal (número): '),
		generateRandomPosition(HPos, 7),
		write(HPos), write('.'), nl,
		write('Posicao Vertical (letra): '),
		generateRandomPosition(VPos, 7),
		write(VPos), write('.'), nl,
		playPieceInPos(Board, HPos, VPos, Element, 1,1, [], FinalBoard);
		(FirstPlay = 1 ->
			write('Posicao Horizontal (número): '),
			generateRandomPosition(HPos, 7),
			write(HPos), write('.'), nl,
			write('Posicao Vertical (letra): '),
			generateRandomPosition(VPos, 7),
			write(VPos), write('.'), nl,
			playPieceInPos(Board, HPos, VPos, Element, 1,1, [], FinalBoard);
			searchAdjacentInBoard(Board, Element, 1, 1, FinalH, FinalV),
			write(FinalH), write(FinalV), nl,
			(FinalH = 0, FinalV = 0 ->
				getColumnsInBoard(Board, 1, [], Columns, Size),
				searchAdjacentInBoard(Columns, Element, 1, 1, NewFinalH, NewFinalV),
				(NewFinalH = 0, NewFinalV = 0 ->
					write('Posicao Horizontal (número): '),
					generateRandomPosition(HPos, 7),
					write(HPos), write('.'), nl,
					write('Posicao Vertical (letra): '),
					generateRandomPosition(VPos, 7),
					write(VPos), write('.'), nl,
					playPieceInPos(Board, HPos, VPos, Element, 1,1, [], FinalBoard);
					write('Posicao Horizontal (número): '),
					write(FinalV), write('.'), nl,
					write('Posicao Vertical (letra): '),
					generateRandomPlayingSpot(FinalH, PlayingH, Size),
					write(PlayingH), write('.'), nl,
					playPieceInPos(Board, NewFinalV, PlayingH, Element, 1,1, [], FinalBoard));
				write('Posicao Horizontal (número): '),
				generateRandomPlayingSpot(FinalH, PlayingH, Size),
				write(PlayingH), write('.'), nl,
				write('Posicao Vertical (letra): '),
				write(FinalV), write('.'), nl,
				playPieceInPos(Board, PlayingH, FinalV, Element, 1,1, [], FinalBoard)))).
	
playingDecisionHardAI(Element, Board, FinalBoard, FirstPlay, Size):-
	(FirstPlay = 1 ->
		write('Posicao Horizontal (número): '),
		generateRandomPosition(HPos, 7),
		write(HPos), write('.'), nl,
		write('Posicao Vertical (letra): '),
		generateRandomPosition(VPos, 7),
		write(VPos), write('.'), nl,
		playPieceInPos(Board, HPos, VPos, Element, 1,1, [], FinalBoard);
		searchAdjacentInBoard(Board, Element, 1, 1, FinalH, FinalV),
		write(FinalH), write(FinalV), nl,
		(FinalH = 0, FinalV = 0 ->
			getColumnsInBoard(Board, 1, [], Columns, Size),
			searchAdjacentInBoard(Columns, Element, 1, 1, NewFinalH, NewFinalV),
			(NewFinalH = 0, NewFinalV = 0 ->
				write('Posicao Horizontal (número): '),
				generateRandomPosition(HPos, 7),
				write(HPos), write('.'), nl,
				write('Posicao Vertical (letra): '),
				generateRandomPosition(VPos, 7),
				write(VPos), write('.'), nl,
				playPieceInPos(Board, HPos, VPos, Element, 1,1, [], FinalBoard);
				write('Posicao Horizontal (número): '),
				write(FinalV), write('.'), nl,
				write('Posicao Vertical (letra): '),
				generateRandomPlayingSpot(FinalH, PlayingH, Size),
				write(PlayingH), write('.'), nl,
				playPieceInPos(Board, NewFinalV, PlayingH, Element, 1,1, [], FinalBoard));
			write('Posicao Horizontal (número): '),
			generateRandomPlayingSpot(FinalH, PlayingH, Size),
			write(PlayingH), write('.'), nl,
			write('Posicao Vertical (letra): '),
			write(FinalV), write('.'), nl,
			playPieceInPos(Board, PlayingH, FinalV, Element, 1,1, [], FinalBoard))).
		

shiftRowDecisionEasyAI(Board, FinalBoard):-
	nl, write('Linha a deslocar: '),
	generateRandomPosition(Line, 7),
	write(Line), write('.'), nl,
	write('Que direcção pretende? 1 - Esquerda | 2 - Direita: '),
	generateRandomDirection(Direction),
	write(Direction), write('.'), nl,
	selectListInBoard(Board, Line, 1, Row),
	shiftRow(Row, Direction, 1, [], FinalRow, 7),
	putBackListInBoard(Board, Line, 1, FinalRow, [], FinalBoard).
	
shiftRowDecisionMediumAI(Board, FinalBoard, Line, Random):-
	(Random = 1 ->
		nl, write('Linha a deslocar: '),
		generateRandomPosition(Line, 7),
		write(Line), write('.'), nl,
		write('Que direcção pretende? 1 - Esquerda | 2 - Direita: '),
		generateRandomDirection(Direction),
		write(Direction), write('.'), nl,
		selectListInBoard(Board, Line, 1, Row),
		shiftRow(Row, Direction, 1, [], FinalRow, 7),
		putBackListInBoard(Board, Line, 1, FinalRow, [], FinalBoard);
		
		nl, write('Linha a deslocar: '),
		write(Line), write('.'), nl,
		write('Que direcção pretende? 1 - Esquerda | 2 - Direita: '),
		generateRandomDirection(Direction),
		write(Direction), write('.'), nl,
		selectListInBoard(Board, Line, 1, Row),
		shiftRow(Row, Direction, 1, [], FinalRow, 7),
		putBackListInBoard(Board, Line, 1, FinalRow, [], FinalBoard)).	

shiftRowDecisionHardAI(Board, FinalBoard, Line):-
	nl, write('Linha a deslocar: '),
	write(Line), write('.'), nl,
	write('Que direcção pretende? 1 - Esquerda | 2 - Direita: '),
	generateRandomDirection(Direction),
	write(Direction), write('.'), nl,
	selectListInBoard(Board, Line, 1, Row),
	shiftRow(Row, Direction, 1, [], FinalRow, 7),
	putBackListInBoard(Board, Line, 1, FinalRow, [], FinalBoard).
	
shiftColumnDecisionEasyAI(Board, FinalBoard):-
	nl, write('Coluna a deslocar: '),
	generateRandomPosition(Column, 7),
	write(Column), write('.'), nl,
	write('Que direcção pretende? 1 - Cima | 2 - Baixo: '),
	generateRandomDirection(Direction),
	write(Direction), write('.'), nl,
	shiftColumn(Board, Column, Direction, FinalBoard).

shiftColumnDecisionMediumAI(Board, FinalBoard, Column, Random):-
	(Random = 1 ->
		nl, write('Coluna a deslocar: '),
		generateRandomPosition(Column, 7),
		write(Column), write('.'), nl,
		write('Que direcção pretende? 1 - Cima | 2 - Baixo: '),
		generateRandomDirection(Direction),
		write(Direction), write('.'), nl,
		shiftColumn(Board, Column, Direction, FinalBoard);
		
		nl, write('Coluna a deslocar: '),
		write(Column), write('.'), nl,
		write('Que direcção pretende? 1 - Cima | 2 - Baixo: '),
		generateRandomDirection(Direction),
		write(Direction), write('.'), nl,
		shiftColumn(Board, Column, Direction, FinalBoard)).
	
shiftColumnDecisionHardAI(Board, FinalBoard, Column):-
	nl, write('Coluna a deslocar: '),
	write(Column), write('.'), nl,
	write('Que direcção pretende? 1 - Cima | 2 - Baixo: '),
	generateRandomDirection(Direction),
	write(Direction), write('.'), nl,
	shiftColumn(Board, Column, Direction, FinalBoard).
	
turnOfEasyAI(AI, AIChar, Board, OtherPlayer, OPChar, AILevel, FirstPlay, Size):-
	nl, nl, printBoard(Board, 1),
	nl, nl, write(AI), nl, nl,
	write('1 - Jogar Peca'), nl,
	write('2 - Trocar Linha'), nl,
	write('3 - Trocar Coluna'), nl,
	write('4 - Desistir'), nl,
	write('Opcao: '),
	generateRandomOption(Option),
	write(Option), write('.'), nl,
	(Option = 1 -> 
		(playingDecisionEasyAI(AIChar, Board, FinalBoard) -> 
			GaveUp is 0;
			write('Jogada Invalida, tente de novo'),
			nl,
			turnOfEasyAI(AI, AIChar, Board, OtherPlayer, OPChar, AILevel, FirstPlay, Size));
		  (Option = 2 -> shiftRowDecisionEasyAI(Board, FinalBoard), GaveUp is 0);
				 (Option = 3 -> shiftColumnDecisionEasyAI(Board, FinalBoard), GaveUp is 0);
						(Option = 4 -> quitDecision(AI, GaveUp); GaveUp is 0)),
	(GaveUp \= 1 ->
		verifyBoard(FinalBoard, 7, AIChar, AI, Res),
		(Res = 0 ->
			PlayerTurn = 1,
			showMenu(OtherPlayer, OPChar, FinalBoard, AI, AIChar, AILevel, PlayerTurn, FirstPlay, Size);
			nl, nl, printBoard(FinalBoard,1), nl, nl, write('End!'));
	betterLuckNextTime).
	
turnOfMediumAI(AI, AIChar, Board, OtherPlayer, OPChar, AILevel, FirstPlay, Size):-
	nl, nl, printBoard(Board, 1),
	nl, nl, write(AI), nl, nl,
	write('1 - Jogar Peca'), nl,
	write('2 - Trocar Linha'), nl,
	write('3 - Trocar Coluna'), nl,
	write('4 - Desistir'), nl,
	write('Opcao: '),
	random(1,3,MediumRandom),
	(MediumRandom = 1 -> generateRandomOption(Option); generateOptionDecision(Option, Board, OPChar, Size, HPos, VPos)),
	write(Option), write('.'), nl,
	(Option = 1 -> 
		(playingDecisionMediumAI(AIChar, Board, FinalBoard, FirstPlay, Size, MediumRandom) -> 
			GaveUp is 0;
			write('Jogada Invalida, tente de novo'),
			nl,
			turnOfMediumAI(AI, AIChar, Board, OtherPlayer, OPChar, AILevel, FirstPlay, Size));
		  (Option = 2 -> shiftRowDecisionMediumAI(Board, FinalBoard, HPos, MediumRandom), GaveUp is 0);
				 (Option = 3 -> shiftColumnDecisionMediumAI(Board, FinalBoard, VPos, MediumRandom), GaveUp is 0);
						(Option = 4 -> quitDecision(AI, GaveUp); GaveUp is 0)),
	(GaveUp \= 1 ->
		verifyBoard(FinalBoard, 7, AIChar, AI, Res),
		(Res = 0 ->
			PlayerTurn = 1,
			showMenu(OtherPlayer, OPChar, FinalBoard, AI, AIChar, AILevel, PlayerTurn, FirstPlay, Size);
			nl, nl, printBoard(FinalBoard,1), nl, nl, write('End!'));
	betterLuckNextTime).
	
turnOfHardAI(AI, AIChar, Board, OtherPlayer, OPChar, AILevel, FirstPlay, Size):-
	nl, nl, printBoard(Board, 1),
	nl, nl, write(AI), nl, nl,
	write('1 - Jogar Peca'), nl,
	write('2 - Trocar Linha'), nl,
	write('3 - Trocar Coluna'), nl,
	write('4 - Desistir'), nl,
	write('Opcao: '),
	generateOptionDecision(Option, Board, OPChar, Size, HPos, VPos),
	write(Option), write('.'), nl,
	(Option = 1 -> 
		(playingDecisionHardAI(AIChar, Board, FinalBoard, FirstPlay, Size) -> 
			GaveUp is 0, (FirstPlay = 1 -> NewFirstPlay is FirstPlay+1; NewFirstPlay is FirstPlay);
			write('Jogada Invalida, tente de novo!'),
			nl,
			turnOfHardAI(AI, AIChar, Board, OtherPlayer, OPChar, AILevel, FirstPlay, Size));
		  (Option = 2 -> shiftRowDecisionHardAI(Board, FinalBoard, HPos), GaveUp is 0);
				 (Option = 3 -> shiftColumnDecisionHardAI(Board, FinalBoard, VPos), GaveUp is 0);
						(Option = 4 -> quitDecision(AI, GaveUp); GaveUp is 0)),
	(GaveUp \= 1 ->
		verifyBoard(FinalBoard, 7, AIChar, AI, Res),
		(Res = 0 ->
			PlayerTurn is 1,
			showMenu(OtherPlayer, OPChar, FinalBoard, AI, AIChar, AILevel, PlayerTurn, NewFirstPlay, Size);
			nl, nl, printBoard(FinalBoard,1), nl, nl, write('End!'));
	betterLuckNextTime).

%---- Start Game ---

start:-
	server(Socket, Stream, 60070),
	startGame(Stream).
	
startGame:-
	chooseMethod(Method),
	(Method = 1 ->
		createPlayers(Player1, Player2, Player1Char, Player2Char),
		initialBoard(Board),
		AI = 0,
		Turn = 1,
		showMenu(Player1, Player1Char, Board, Player2, Player2Char, AI, Turn, 1, 7);
		(Method = 2 -> 
			askDificulty(Dif),
			initialBoard(Board),
			createSinglePlayer(Player1, AIPlayer, Player1Char, AIChar, Dif),
			Turn = 1,
			showMenu(Player1, Player1Char, Board, AIPlayer, AIChar, Dif, Turn, 1, 7))).
	
chooseMethod(Method):-
	nl, write('Como quer jogar?'), nl,
	write('1 - Humano vs Humano'), nl,
	write('2 - Humano vs AI'), nl,
	write('3 - AI vs AI'), nl,
	write('Opcao: '),
	read(Method),
	nl, nl.
	
createSinglePlayer(Player1, Player2, Player1Char, Player2Char, AIDif):-
	write('Nome do Jogador 1: '),
	read(Player1), nl,
	Player1Char = 'A',
	(AIDif = 1 ->
		Player2 = 'Computador Modo Fácil';
		(AIDif = 2 ->
			Player2 = 'Computador Modo Médio';
			(AIDif = 3 ->
				Player2 = 'Computador Modo Dificil'))),
	Player2Char = 'B'.
			
createPlayers(Player1, Player2, Player1Char, Player2Char):-
	write('Nome do Jogador 1: '),
	read(Player1), nl,
	Player1Char = 'A',
	write('Nome do Jogador 2: '),
	read(Player2), nl,
	Player2Char = 'B'.

convertVPos(HPos, HPosFinal):-
		atom_codes(HPos, [Head|_]),
        HPosFinal is Head-96.

playingDecision(Element, Board, FinalBoard):-
	write('Posicao Horizontal (número): '),
	read(HPos),
	write('Posicao Vertical (letra): '),
	read(VPos),
	convertVPos(VPos, VPosFinal),
	playPieceInPos(Board, HPos, VPosFinal, Element, 1,1, [], FinalBoard).
		  
quitDecision(Player, GaveUp):-
		write('O jogador '),
		write(Player), 
		write(' desistiu do jogo.'),
		GaveUp = 1.
				
shiftRowDecision(Board, FinalBoard):-
		nl, write('Linha a deslocar: '),
		read(Line), nl,
		write('Que direcção pretende? 1 - Esquerda | 2 - Direita: '),
		read(Direction),
		selectListInBoard(Board, Line, 1, Row),
		shiftRow(Row, Direction, 1, [], FinalRow, 7),
		putBackListInBoard(Board, Line, 1, FinalRow, [], FinalBoard).
		
shiftColumnDecision(Board, FinalBoard):-
		nl, write('Coluna a deslocar: '),
		read(Column), nl,
		convertVPos(Column, FinalColumn),
		write('Que direcção pretende? 1 - Cima | 2 - Baixo: '),
		read(Direction),
		shiftColumn(Board, FinalColumn, Direction, FinalBoard).
		
victoryMessage(Player):-
	nl, write('Parabens, '), write(Player), write(' ganhou o jogo!').
	
betterLuckNextTime:-
	nl, write('Boa sorte para a proxima!').
	
restartGame:-
	nl, write('Jogo a Recomeçar...'), nl, nl, nl, nl, nl, nl, nl, nl, nl, nl, nl, startGame.
		
showMenu(Player, PlayerChar, Board, OtherPlayer, OPChar, AILevel, PlayerTurn, FirstPlay, Size):-
	(PlayerTurn \= 1 ->
		(AILevel = 1 -> 
			turnOfEasyAI(Player, PlayerChar, Board, OtherPlayer, OPChar, AILevel, FirstPlay, Size);
			(AILevel = 2 ->
				turnOfMediumAI(Player, PlayerChar, Board, OtherPlayer, OPChar, AILevel, FirstPlay, Size);
				(AILevel = 3 ->
					turnOfHardAI(Player, PlayerChar, Board, OtherPlayer, OPChar, AILevel, FirstPlay, Size))));
			nl, nl, printBoard(Board, 1),
			nl, nl, write(Player), nl, nl,
			write('1 - Jogar Peca'), nl,
			write('2 - Trocar Linha'), nl,
			write('3 - Trocar Coluna'), nl,
			write('4 - Desistir'), nl,
			write('5 - Recomeçar Jogo'), nl,
			write('Opcao: '),
			read(Option),
			(Option = 1 -> 
				(playingDecision(PlayerChar, Board, FinalBoard) -> 
					GaveUp is 0;
					write('Jogada Invalida, tente de novo'),
					nl,
					showMenu(Player, PlayerChar, Board, OtherPlayer, OPChar, AILevel, PlayerTurn, FirstPlay, Size));
				  (Option = 2 -> shiftRowDecision(Board, FinalBoard), GaveUp is 0;
						 (Option = 3 -> shiftColumnDecision(Board, FinalBoard), GaveUp is 0;
								(Option = 4 -> quitDecision(Player, GaveUp);
									(Option = 5 -> restartGame, !))))),
			(GaveUp = 0 ->
				verifyBoard(FinalBoard, 7, PlayerChar, Player, Res),
				(Res = 0 ->
					(AILevel > 0 ->
						NewTurn = 0,
						showMenu(OtherPlayer, OPChar, FinalBoard, Player, PlayerChar, AILevel, NewTurn, FirstPlay, Size);
						showMenu(OtherPlayer, OPChar, FinalBoard, Player, PlayerChar, AILevel, PlayerTurn, FirsPlay, Size));
					nl, nl, printBoard(FinalBoard,1), nl, nl, write('End!'));
				(GaveUp = 1 -> betterLuckNextTime; restartGame))).

%------Socket Predicates-----

%First String - Method+Player1+Player2

startGame(Stream):-
	readFromSocket(Stream, FirstString),
	(Method = 1 ->
		createPlayers(Player1, Player2, Player1Char, Player2Char),
		initialBoard(Board),
		AI = 0,
		Turn = 1,
		showMenu(Player1, Player1Char, Board, Player2, Player2Char, AI, Turn, 1, 7);
		(Method = 2 -> 
			askDificulty(Dif),
			initialBoard(Board),
			createSinglePlayer(Player1, AIPlayer, Player1Char, AIChar, Dif),
			Turn = 1,
			showMenu(Player1, Player1Char, Board, AIPlayer, AIChar, Dif, Turn, 1, 7))).
			
%prologstring -> Board + Name + Player(1)/AI(0) + Char 