# java-lotto
로또 미션 진행을 위한 저장소

## Done
* 일반로또 입력
	1. Lotto 테이블에 데이터(type) 삽입
	2. LottoNumber 테이블에 한 로또에 대한 번호 6개 삽입
	3. 생성된 Lotto의 id 받아서 Result에 넣기
* 당첨로또 입력
  1. WinngLotto 테이블에 데이터(type) 삽입
	2. LottoNumber 테이블에 한 로또에 대한 번호 6개 삽입
	3. 생성된 Lotto의 id 받아서 Result에 넣기
* 사용자가 게임을 시작했을 때,
  * Lotto 테이블: type, LottoNumber 테이블: number에 값을 넣고
  * LottoGame 테이블: week, money에 값을 넣고
* 사용자가 당첨 로또를 입력했을 때, 
  * Lotto 테이블에 로또 정보 넣고
  * LottoNumber 테이블에 당첨번호 넣고
  * WinningLotto 테이블에 나머지 정보
  넣고
#### dao
* 당첨 로또를 입력받으면
  *   당첨 로또 정보 insert
  *  게임 정보 insert
  * 회차 정보
* **WinningLottoDAO**
  *   당첨 로또 insert
	    1. Lotto 테이블에 로또 정보 넣고
	    2. LottoNumber 테이블에 당첨번호 넣고
	    3. WinningLotto 테이블에 나머지 정보 넣고
    * 당첨 로또 id에 대한 데이터 꺼내서 리턴하기
* **LottoDAO**
  *   일반 로또 insert
	    1. Lotto 테이블에 로또 정보 넣고
	    2. LottoNumber 테이블에 당첨번호 넣고
#### view
* 구입 금액 입력 받았을 때,
    * 자연수인지 확인하기
* 수동 로또 입력값을 받았을 때,
    * 자연수인지 확인하기
    * 빈칸 없애기
    * **[예외]** 로또 번호가 6개인지 확인 -> IllegalAmountOfNumberException
    * **[예외]** 최대로 구입 가능한 개수보다 많이 입력됐을 경우 -> UnexpectedInputRangeException
* 당첨 번호를 입력했을 때,
    * null인지 확인
    * isEmpty 확인
    * 빈칸 없애기
#### domain
* **Money**
    * 구입 금액 저장하기
    * 구입 금액이 해당 조건에 부합하는지 확인하기
    * **[예외]** 입력값이 1000미만일 경우 -> UnexpectedInputRangeException
        * -13 -> error!
        * 4.4 -> error!
    * 로또 게임 횟수 리턴하기
        * 14000 -> 14
    * 당첨금 누적하기
        * 5000 -> 5000
        * 2000 -> 7000
    * 수익률 계산하기
* **LottoTicketFactory**
    * 수동 로또인지 자동 로또인지 구분해서 객체 생성하기
    * **수동 로또의 경우**
        * String으로 로또 번호 6개가 입력되면 LottoNumber 리스트에 저장하기
            * "1,2,3,4,5,6" -> [1,2,3,4,5,6]
            * "1,2,3,4,5" -> error!
            *  **[예외]** 입력된 값에 자연수 외에 다른 값이 있는지 확인 -> UnexpectedInputException
            * **[예외]** 중복된 숫자가 있는지 확인 -> DuplicatedInputException
                * "1,2,3,4,5,5" -> error!
* **LottoTicket**: 로또 한 장
    * 당첨 번호와 일치하는 수 카운트해서 리턴하기
    * 보너스 볼과 일치하는 수가 있는지 확인해서 boolean 리턴하기
* **LottoTickets**: 로또 리스트, 구매할 수동 로또 수
    * 입력한 수동 로또 개수만큼 번호가 입력됐는지 boolean
* **LottoNumber**
    * 요청한 로또 번호에 대한 객체 리턴
        * LottoNumber.getNumber(3) -> new LottoNumber(3)
    * **[예외]** 요청한 로또 번호가 1~45가 아닐 경우 -> UnexpectedInputRangeException
    * **수동 로또의 경우**
    * **자동 로또의 경우**
        * subList해서 6개 전달하는 함수 만들기
            * getRandomNumbers() -> return List<LottoNumber>
* **WinningLotto**
    * String으로 로또 번호 6개가 입력되면 LottoNumber 리스트에 저장하기
        * "1,2,3,4,5,6" -> [1,2,3,4,5,6]
        * "1,2,3,4,5,5" -> error!
        * "1,2,3,4,5" -> error!
    * **[예외]** 입력된 값에 자연수 외에 다른 값이 있는지 확인 -> UnexpectedInputException
    * **[예외]** 중복된 숫자가 있는지 확인 -> DuplicatedInputException
    * 파라미터로 전달받은 번호가 당첨번호와 일치하는지 boolean으로 리턴
        * 일반 번호 확인
        * 보너스 볼 확인
* **RankType**: Enum
    * 일치하는 번호 수에 따라 다르게 객체 리턴하기
        * valueOf(5, true) -> RankType.SECOND
        * valueOf(6, false) -> RankType.FIRST
    * 일치하는 번호 수 리턴하기 
* **WinStatistics**
    * 당첨 시 집계하기
    * 수익률 계산하기