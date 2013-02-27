import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import java.util.HashMap;
import java.util.Map;
import processing.core.PApplet;

public class SketchAudio
{
  private Minim _minim;
  private Map<String, AudioPlayer> _tones = new HashMap<String, AudioPlayer>();

  private SketchAudio(PApplet parent)
  {
    _minim = new Minim(parent);

    _tones.put("lowC", _minim.loadFile("data/lowC.mp3"));
    _tones.put("lowE", _minim.loadFile("data/lowE.mp3"));
    _tones.put("lowG", _minim.loadFile("data/lowG.mp3"));

    _tones.put("middleC", _minim.loadFile("data/middleC.mp3"));
    _tones.put("middleE", _minim.loadFile("data/middleE.mp3"));
    _tones.put("middleG", _minim.loadFile("data/middleG.mp3"));

    _tones.put("highC", _minim.loadFile("data/highC.mp3"));
    _tones.put("highE", _minim.loadFile("data/highE.mp3"));
  }
}
