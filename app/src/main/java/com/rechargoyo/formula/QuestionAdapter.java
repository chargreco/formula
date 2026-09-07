package com.rechargoyo.formula;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Adaptador para el {@link RecyclerView} del Cuestionario Simple ({@link Quest}).
 * Muestra cada pregunta con su enunciado y un grupo de botones de opción ({@link RadioGroup}) para seleccionar respuesta.
 *
 * @author Eduardo
 * @version 1.0
 */
public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {

    /** Lista de preguntas de opción múltiple. */
    private final ArrayList<Question> questionList;

    /**
     * Construye un adaptador para el cuestionario.
     *
     * @param questionList Lista de preguntas {@link Question}.
     */
    public QuestionAdapter(ArrayList<Question> questionList) {
        this.questionList = questionList;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_item, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        Question question = questionList.get(position);
        holder.bind(question);
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    /**
     * ViewHolder que gestiona la vista de cada pregunta y la selección de botones de radio.
     */
    static class QuestionViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewQuestion;
        private final RadioGroup radioGroupAnswers;
        private final RadioButton[] radioButtons = new RadioButton[4];

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewQuestion = itemView.findViewById(R.id.textViewQuestion);
            radioGroupAnswers = itemView.findViewById(R.id.radioGroupAnswers);
            radioButtons[0] = itemView.findViewById(R.id.radioButton1);
            radioButtons[1] = itemView.findViewById(R.id.radioButton2);
            radioButtons[2] = itemView.findViewById(R.id.radioButton3);
            radioButtons[3] = itemView.findViewById(R.id.radioButton4);
        }

        /**
         * Vincula los datos de la pregunta con las vistas de radio buttons del ítem.
         *
         * @param question Objeto {@link Question} con las respuestas a mostrar.
         */
        public void bind(Question question) {
            textViewQuestion.setText(question.getQuestionText());
            ArrayList<String> answers = question.getAnswers();
            for (int i = 0; i < radioButtons.length; i++) {
                radioButtons[i].setText(answers.get(i));
                radioButtons[i].setOnClickListener(v -> {
                    int selectedAnswer = radioGroupAnswers.indexOfChild(v);
                    question.setSelectedOptionIndex(selectedAnswer);
                });
            }
        }
    }
}
