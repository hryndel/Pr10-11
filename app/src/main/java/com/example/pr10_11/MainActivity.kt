package com.example.pr10_11

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.example.pr10_11.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var mainHandler : Handler
    private lateinit var binding: ActivityMainBinding
    private val variants = arrayOf(R.drawable.rock, R.drawable.paper, R.drawable.scissors, R.drawable.lizard, R.drawable.spock)
    private val colors = arrayOf(intArrayOf(Color.YELLOW,Color.GREEN,Color.RED,Color.RED,Color.GREEN), intArrayOf(Color.RED,Color.YELLOW,Color.GREEN,Color.GREEN,Color.RED), intArrayOf(Color.GREEN,Color.RED,Color.YELLOW,Color.RED,Color.GREEN), intArrayOf(Color.GREEN,Color.RED,Color.GREEN,Color.YELLOW,Color.RED), intArrayOf(Color.RED,Color.GREEN,Color.RED,Color.GREEN,Color.YELLOW))
    private val rules = arrayOf(arrayOf(R.drawable.b_draw, R.drawable.b_paper_vs_rock, R.drawable.b_rock_vs_scissors, R.drawable.b_rock_vs_lizard, R.drawable.b_spock_vs_rock), arrayOf(R.drawable.b_paper_vs_rock, R.drawable.b_draw, R.drawable.b_scissors_vs_paper, R.drawable.b_lizard_vs_paper, R.drawable.b_paper_vs_spock), arrayOf(R.drawable.b_rock_vs_scissors, R.drawable.b_scissors_vs_paper, R.drawable.b_draw, R.drawable.b_scissors_vs_lizard, R.drawable.b_spock_vs_scissors), arrayOf(R.drawable.b_rock_vs_lizard, R.drawable.b_lizard_vs_paper, R.drawable.b_scissors_vs_lizard, R.drawable.b_draw, R.drawable.b_lizard_vs_spock), arrayOf(R.drawable.b_spock_vs_rock, R.drawable.b_paper_vs_spock, R.drawable.b_spock_vs_scissors, R.drawable.b_lizard_vs_spock, R.drawable.b_draw))
    private var click: Boolean = true
    private var pX: Int = 0
    private var rY: Int = 0
    private var win: Int = 0
    private var lose: Int = 0
    private var draw: Int = 0
    private var secondsLeft:Int = 0
    private val updateTextTask = object :Runnable{
        override fun run() {
            TimerTick()
            mainHandler.postDelayed(this,1000)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(updateTextTask)
        super.onResume()
    }
    fun TimerTick():Int = secondsLeft++
    fun Generate(view: View){
        if (click){
            if((binding.imgPlayer.getDrawable().getConstantState() != getResources().getDrawable(R.drawable.start_img).getConstantState())){
                rY = (0..4).random()
                binding.imgRobot.setImageResource(variants[rY])
                click = false
                Winner()
            }
            else DoChoice("Сделайте выбор", Color.RED)
        }
        else Clear()
    }
    fun Clear(){
        binding.imgRobot.setImageResource(R.drawable.start_img)
        binding.imgPlayer.setImageResource(R.drawable.start_img)
        binding.imgBattle.setImageResource(R.drawable.start_rules_img)
        binding.linearLayoutAnswer.setBackgroundColor(Color.WHITE)
        click = true
    }
    fun ChoicePlayer(view: View?){
        pX = when (view?.id){
            R.id.btnRock -> 0
            R.id.btnPaper -> 1
            R.id.btnScissors -> 2
            R.id.btnLizard -> 3
            R.id.btnSpock -> 4
                else -> 0
        }
        binding.imgPlayer.setImageResource(variants[pX])
        DoChoice(getString(R.string.textStart), Color.parseColor("#FF6200EE"))
    }
    fun DoChoice(text: String, value: Int){
        binding.btnStart.text = text
        binding.btnStart.setBackgroundColor(value)
    }
    fun Winner(){
        binding.linearLayoutAnswer.setBackgroundColor(colors[rY][pX])
        binding.imgBattle.setImageResource(rules[rY][pX])
        if (colors[rY][pX] == Color.GREEN) win++
        else if (colors[rY][pX] == Color.RED) lose++
        else draw++
        pX = 0
        rY = 0
    }
    fun ShowStat(view: View){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Статистика")
        builder.setMessage("Победы: $win\nПоражения: $lose\nНичьи: $draw\nВремя сессии: $secondsLeft сек.")
        builder.setPositiveButton("ОК") {
                    dialog, id ->  dialog.cancel()
            }
        builder.show()
    }
}