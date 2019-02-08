package ru.geekbrains.gkportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.geekbrains.gkportal.entity.questionnaire.Answer;
import ru.geekbrains.gkportal.entity.questionnaire.Question;
import ru.geekbrains.gkportal.entity.questionnaire.Questionnaire;
import ru.geekbrains.gkportal.service.AnswerService;
import ru.geekbrains.gkportal.service.ContactService;
import ru.geekbrains.gkportal.service.QuestionnaireService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/rest")
public class TestRest {

    private ContactService contactService;
    private AnswerService answerService;
    private QuestionnaireService questionnaireService;

    @Autowired
    public void setContactService(ContactService contactService) {
        this.contactService = contactService;
    }

    @Autowired
    public void setAnswerService(AnswerService answerService) {
        this.answerService = answerService;
    }

    @Autowired
    public void setQuestionnaireService(QuestionnaireService questionnaireService) {
        this.questionnaireService = questionnaireService;
    }

    @GetMapping("test")
    public Questionnaire permitAllPage(Model model) {
        List<String> uuidList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            uuidList.add(UUID.randomUUID().toString());
        }

        model.addAttribute("uuids", uuidList);


        List<Answer> answerList1 = new ArrayList<>(Arrays.asList(
                new Answer("ЗА", 1),
                new Answer("ПРОТИВ", 2),
                new Answer("Воздержался", 3),
                new Answer("Я не принимаю участия в голосовании (бюллетень остался у меня)", 4)
        ));

        List<Answer> answerList2 = answerService.copyAnswerList(answerList1);
        List<Answer> answerList3 = answerService.copyAnswerList(answerList1);
        List<Answer> answerList4 = answerService.copyAnswerList(answerList1);
        List<Answer> answerList5 = new ArrayList<>(Arrays.asList(
                new Answer("Проголосовал. Бюллетень передал/передам в УК.", 1),
                new Answer("Я не принимаю участия в голосовании (бюллетень остался у меня)", 2)
        ));

        List<Answer> answerList6 = new ArrayList<>(Arrays.asList(
                new Answer("Первом корпусе", 1),
                new Answer("Втором корпусе", 2),
                new Answer("Третьем корпусе (колодец)", 3),
                new Answer("Паркинге", 4)
        ));

        Question question1 = Question.builder()
                .name("Ваше решение по работе УК Еврогород *")
                .sortNumber(3)
                .required(true)
                .single(true)
                .externalNumber(3)
                .answers(answerList1)
                .build();

        Question question2 = Question.builder()
                .name("Ваше решение о ставке 35 рублей за квадратный метр *")
                .sortNumber(4)
                .required(true)
                .single(true)
                .externalNumber(4)
                .answers(answerList2)
                .build();

        Question question3 = Question.builder()
                .name("Ваше решение об охране? *")
                .sortNumber(5)
                .required(true)
                .single(true)
                .externalNumber(5)
                .answers(answerList3)
                .build();

        Question question4 = Question.builder()
                .name("Ваше решение об электронном голосовании? *")
                .sortNumber(6)
                .required(true)
                .single(true)
                .externalNumber(6)
                .answers(answerList4)
                .build();


        Question question5 = Question.builder()
                .name("Вы проголосовали или будете игнорировать голосование? *")
                .sortNumber(2)
                .required(true)
                .single(true)
                .externalNumber(2)
                .answers(answerList5)
                .build();

        Question question6 = Question.builder()
                .name("Объект вашей собственности расположен в (строительные номера корпусов) *")
                .sortNumber(1)
                .required(true)
                .single(true)
                .externalNumber(2)
                .answers(answerList6)
                .build();

        answerList1.forEach(answer -> answer.setQuestion(question1));
        answerList2.forEach(answer -> answer.setQuestion(question2));
        answerList3.forEach(answer -> answer.setQuestion(question3));
        answerList4.forEach(answer -> answer.setQuestion(question4));
        answerList5.forEach(answer -> answer.setQuestion(question5));
        answerList6.forEach(answer -> answer.setQuestion(question6));
        List<Question> questionList = Arrays.asList(question1, question2, question3, question4, question5, question6);
//        questionList.sort(Comparator.comparingInt(Question::getSortNumber));

        Questionnaire questionnaire = Questionnaire.builder()
                .name("Опрос о голосовании (exit pool) собственников ЖК Город")
                .from(LocalDateTime.now())
                .to(LocalDateTime.now().plusMonths(1L))
                .description("Члены инициативной группы входящие в состав счетной комиссии выборочно проверят " +
                        "соответствие информации в данном опросе и в бюллетенях в момент подведения итогов первого " +
                        "общего собрания собственников. Доступ к данным имеют ограниченное число членов инициативной " +
                        "группы ЖК Город. Форму должен заполнить каждый собственник отдельно по каждому объекту " +
                        "недвижимости(квартиры, машиноместа), где у него есть доля.")
                .open(true)
                .active(true)
                .inBuildNum(true)
                .useRealEstate(true)
                .questions(questionList)
                .build();

        question1.setQuestionnaire(questionnaire);
        question2.setQuestionnaire(questionnaire);
        question3.setQuestionnaire(questionnaire);
        question4.setQuestionnaire(questionnaire);
        question5.setQuestionnaire(questionnaire);
        question6.setQuestionnaire(questionnaire);

        questionnaireService.save(questionnaire);


        return questionnaire;
    }
}