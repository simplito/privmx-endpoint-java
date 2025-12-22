/*
package com.simplito.java.privmx_endpoint_streams.modules;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.simplito.java.privmx_endpoint.model.exceptions.PrivmxException;
import com.simplito.java.privmx_endpoint.model.UserWithPubKey;
import com.simplito.java.privmx_endpoint.modules.core.Connection;
import com.simplito.java.privmx_endpoint.modules.event.EventApi;

import org.appspot.apprtc.RecordedAudioToFileController;
import org.webrtc.AudioSource;
import org.webrtc.AudioTrack;
import org.webrtc.CameraVideoCapturer;
import org.webrtc.DefaultVideoDecoderFactory;
import org.webrtc.DefaultVideoEncoderFactory;
import org.webrtc.EglBase;
import org.webrtc.IceCandidate;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStreamTrack;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.RtpSender;
import org.webrtc.SessionDescription;
import org.webrtc.SoftwareVideoDecoderFactory;
import org.webrtc.SoftwareVideoEncoderFactory;
import org.webrtc.SurfaceTextureHelper;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoDecoderFactory;
import org.webrtc.VideoEncoderFactory;
import org.webrtc.VideoSink;
import org.webrtc.VideoSource;
import org.webrtc.VideoTrack;
import org.webrtc.audio.AudioDeviceModule;
import org.webrtc.audio.JavaAudioDeviceModule;
import org.webrtc.audio.JavaAudioDeviceModule.AudioRecordErrorCallback;
import org.webrtc.audio.JavaAudioDeviceModule.AudioRecordStateCallback;
import org.webrtc.audio.JavaAudioDeviceModule.AudioTrackErrorCallback;
import org.webrtc.audio.JavaAudioDeviceModule.AudioTrackStateCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

*
 * Peer connection client implementation.
 *
 * <p>All public methods are routed to local looper thread.
 * All PeerConnectionEvents callbacks are invoked from the same looper thread.
 * This class is a singleton.


public class PmxPeerConnectionClient2 {
  public static final String VIDEO_TRACK_ID = "ARDAMSv0";
  public static final String AUDIO_TRACK_ID = "ARDAMSa0";
  public static final String VIDEO_TRACK_TYPE = "video";
  private static final String TAG = "PCRTCClient";

  // Executor thread is started once in private ctor and is used for all
  // peer connection API calls to ensure new peer connection factory is
  // created on the same thread as previously destroyed factory.
  private static final ExecutorService executor = Executors.newSingleThreadExecutor();

  private final Timer statsTimer = new Timer();
  private final Timer dropTimer = new Timer();

  private final EglBase rootEglBase;
  private final Context appContext;

  @Nullable
  private PeerConnectionFactory factory;
  @Nullable
  private AudioSource audioSource;
  @Nullable private SurfaceTextureHelper surfaceTextureHelper;
  @Nullable private VideoSource videoSource;
//  private boolean preferIsac;
  private boolean videoCapturerStopped;
  private boolean isError;
  @Nullable
  private VideoSink localRender;
//  @Nullable private List<VideoSink> remoteSinks;
  private TrackObserverImpl trackObserver;
//  private int remoteSinkId;
//  private SignalingParameters signalingParameters;
//  private int videoWidth;
//  private int videoHeight;
//  private int videoFps;
  private MediaConstraints audioConstraints;
  private MediaConstraints sdpMediaConstraints;
  // Queued remote ICE candidates are consumed only after both local and
  // remote descriptions are set. Similarly local ICE candidates are sent to
  // remote peer after both local and remote description are set.
  @Nullable
  private List<IceCandidate> queuedRemoteCandidates;
  private boolean isInitiator;
  @Nullable private SessionDescription localDescription; // either offer or answer description
  @Nullable
  private VideoCapturer videoCapturer;
  // enableVideo is set to true if video should be rendered and sent.
  private boolean renderVideo = true;
  @Nullable
  private VideoTrack localVideoTrack;
//  @Nullable
//  private VideoTrack remoteVideoTrack;
  @Nullable
  private RtpSender localVideoSender;
  // enableAudio is set to true if audio should be sent.
  private boolean enableAudio = true;
  @Nullable
  private AudioTrack localAudioTrack;
  // Implements the WebRtcAudioRecordSamplesReadyCallback interface and writes
  // recorded audio samples to an output file.
  @Nullable private RecordedAudioToFileController saveRecordedAudioToFile;

  private final String bridgeUrl;
  private final String solutionId;
  private final String contextId;
  private final String userPrivKey;
  private final String adminPrivKey;
  private final String streamRoomId;
  private Connection connection;
  private StreamApiLow streamApi;
  private Long localStreamId1;
  private Long localStreamId2;
  private StreamApi api;

*
   * Create a PeerConnectionClient with the specified parameters. PeerConnectionClient takes
   * ownership of `eglBase`.


  public PmxPeerConnectionClient2(Context appContext, EglBase eglBase, String bridgeUrl, String solutionId, String contextId, String userPrivKey, String adminPrivKey, String streamRoomId) {
    this.rootEglBase = eglBase;
    this.appContext = appContext;
    this.bridgeUrl = bridgeUrl;
    this.solutionId = solutionId;
    this.contextId = contextId;
    this.userPrivKey = userPrivKey;
    this.adminPrivKey = adminPrivKey;
    this.streamRoomId = streamRoomId;

    executor.execute(() -> {
      PeerConnectionFactory.initialize(
          PeerConnectionFactory.InitializationOptions.builder(appContext)
              .createInitializationOptions());
    });

    audioConstraints = new MediaConstraints();
  }

*
   * This function should only be called once.


  public void createPeerConnectionFactory(PeerConnectionFactory.Options options) {
    if (factory != null) {
      throw new IllegalStateException("PeerConnectionFactory has already been constructed");
    }
    executor.execute(() -> createPeerConnectionFactoryInternal(options));
  }

  class TrackObserverImpl implements StreamApi.TrackObserver {
    private List<VideoSink> remoteSinks;
    private int remoteSinkId;

    TrackObserverImpl(List<VideoSink> videoSinks) {
      this.remoteSinks = videoSinks;
      this.remoteSinkId = 0;
    }

    @Override
    public void onTrack(MediaStreamTrack track) {
      if (track.kind().equals(VIDEO_TRACK_TYPE)) {
        VideoTrack vt = (VideoTrack) track;
        if (remoteSinks.size() > remoteSinkId) {
          vt.addSink(remoteSinks.get(remoteSinkId));
          ++remoteSinkId;
        }
      }
    }
  }


  public void createPeerConnection2(final VideoSink renderer, final List<VideoSink> remoteSinks, final VideoCapturer videoCapturer) { // WIP
    this.localRender = renderer;
    this.videoCapturer = videoCapturer;
    this.trackObserver = new TrackObserverImpl(remoteSinks);

    connection = Connection.connect(
            userPrivKey,
            solutionId,
            bridgeUrl
    );
    var id = connection.getConnectionId();
    EventApi eventApi = new EventApi(connection);
    StreamApiLow api2 = StreamApiLow.create(connection, eventApi);
    streamApi = api2;
    try { // TODO: FIX this
      while (factory == null) Thread.sleep(200);
    } catch (java.lang.InterruptedException ignore) {}
    api = new StreamApi(appContext, rootEglBase, connection, api2, factory);

    try {
      var room = api.getStreamRoom(streamRoomId);
    } catch (PrivmxException e) {
      //Login as admin and add main account to this room (unnecessary)
      if (e.getCode() == 4026531888L) {
        var connection2 = Connection.connect(
                adminPrivKey,
                solutionId,
                bridgeUrl
        );
        EventApi eventApi2 = new EventApi(connection2);
        StreamApiLow low2 = StreamApiLow.create(connection2, eventApi2);
        var streamApi2 = new StreamApi(appContext, rootEglBase, connection, low2, factory);
        var contextUsers = connection2.getContextUsers(contextId);
        List<UserWithPubKey> users = new ArrayList<UserWithPubKey>();
        for (var user : contextUsers) {
          users.add(user.user);
        }
        streamApi2.updateStreamRoom(streamRoomId, users, users, new byte[]{}, new byte[]{}, 0, true, false, null);
        connection2.disconnect();
      }
    }

    localStreamId1 = api.createStream(streamRoomId);
    var videoTrack = createVideoTrack(videoCapturer);
    var audioTrack = createAudioTrack();
    api.addTrack(localStreamId1, videoTrack, null);
    api.addTrack(localStreamId1, audioTrack, null);
    api.publishStream(localStreamId1);

    executor.execute(() -> {
      CameraVideoCapturer v = (CameraVideoCapturer)videoCapturer;
      v.switchCamera(null);
    });

    dropTimer.schedule(wrap(() -> {
      Log.d(TAG,"joinStream=");
      var streams = api.listStreams(streamRoomId);
      List<Long> streamsToJoin = new ArrayList<Long>();
      for (var stream : streams) {
        streamsToJoin.add(stream.streamId);
      }
      localStreamId2 = api.joinStreamRoom(streamRoomId, streamsToJoin, trackObserver, null);
      Log.d(TAG,"joinStream=END");
    }), (long)1000);

  }
  private static TimerTask wrap(Runnable r) {
    return new TimerTask() {

      @Override
      public void run() {
        r.run();
      }
    };
  }


  public void close() {
    executor.execute(this ::closeInternal);
  }

  private void createPeerConnectionFactoryInternal(PeerConnectionFactory.Options options) {
    isError = false;

    final AudioDeviceModule adm = createJavaAudioDevice();

    // Create peer connection factory.
    if (options != null) {
      Log.d(TAG, "Factory networkIgnoreMask option: " + options.networkIgnoreMask);
    }
    final boolean enableH264HighProfile = false;
    final VideoEncoderFactory encoderFactory;
    final VideoDecoderFactory decoderFactory;

    if (true) {
      encoderFactory = new DefaultVideoEncoderFactory(
          rootEglBase.getEglBaseContext(), true
 enableIntelVp8Encoder
, enableH264HighProfile);
      decoderFactory = new DefaultVideoDecoderFactory(rootEglBase.getEglBaseContext());
    } else {
      encoderFactory = new SoftwareVideoEncoderFactory();
      decoderFactory = new SoftwareVideoDecoderFactory();
    }

    factory = PeerConnectionFactory.builder()
                  .setOptions(options)
                  .setAudioDeviceModule(adm)
                  .setVideoEncoderFactory(encoderFactory)
                  .setVideoDecoderFactory(decoderFactory)
                  .createPeerConnectionFactory();
    Log.d(TAG, "Peer connection factory created.");
    adm.release();
  }

  AudioDeviceModule createJavaAudioDevice() {
    // Enable/disable OpenSL ES playback.
//    if (!peerConnectionParameters.useOpenSLES) {
//      Log.w(TAG, "External OpenSLES ADM not implemented yet.");
//      // TODO(magjed): Add support for external OpenSLES ADM.
//    }

    // Set audio record error callbacks.
    AudioRecordErrorCallback audioRecordErrorCallback = new AudioRecordErrorCallback() {
      @Override
      public void onWebRtcAudioRecordInitError(String errorMessage) {
        Log.e(TAG, "onWebRtcAudioRecordInitError: " + errorMessage);
        reportError(errorMessage);
      }

      @Override
      public void onWebRtcAudioRecordStartError(
          JavaAudioDeviceModule.AudioRecordStartErrorCode errorCode, String errorMessage) {
        Log.e(TAG, "onWebRtcAudioRecordStartError: " + errorCode + ". " + errorMessage);
        reportError(errorMessage);
      }

      @Override
      public void onWebRtcAudioRecordError(String errorMessage) {
        Log.e(TAG, "onWebRtcAudioRecordError: " + errorMessage);
        reportError(errorMessage);
      }
    };

    AudioTrackErrorCallback audioTrackErrorCallback = new AudioTrackErrorCallback() {
      @Override
      public void onWebRtcAudioTrackInitError(String errorMessage) {
        Log.e(TAG, "onWebRtcAudioTrackInitError: " + errorMessage);
        reportError(errorMessage);
      }

      @Override
      public void onWebRtcAudioTrackStartError(
          JavaAudioDeviceModule.AudioTrackStartErrorCode errorCode, String errorMessage) {
        Log.e(TAG, "onWebRtcAudioTrackStartError: " + errorCode + ". " + errorMessage);
        reportError(errorMessage);
      }

      @Override
      public void onWebRtcAudioTrackError(String errorMessage) {
        Log.e(TAG, "onWebRtcAudioTrackError: " + errorMessage);
        reportError(errorMessage);
      }
    };

    // Set audio record state callbacks.
    AudioRecordStateCallback audioRecordStateCallback = new AudioRecordStateCallback() {
      @Override
      public void onWebRtcAudioRecordStart() {
        Log.i(TAG, "Audio recording starts");
      }

      @Override
      public void onWebRtcAudioRecordStop() {
        Log.i(TAG, "Audio recording stops");
      }
    };

    // Set audio track state callbacks.
    AudioTrackStateCallback audioTrackStateCallback = new AudioTrackStateCallback() {
      @Override
      public void onWebRtcAudioTrackStart() {
        Log.i(TAG, "Audio playout starts");
      }

      @Override
      public void onWebRtcAudioTrackStop() {
        Log.i(TAG, "Audio playout stops");
      }
    };

    return JavaAudioDeviceModule.builder(appContext)
        .setSamplesReadyCallback(saveRecordedAudioToFile)
        .setAudioRecordErrorCallback(audioRecordErrorCallback)
        .setAudioTrackErrorCallback(audioTrackErrorCallback)
        .setAudioRecordStateCallback(audioRecordStateCallback)
        .setAudioTrackStateCallback(audioTrackStateCallback)
        .createAudioDeviceModule();
  }

  private void closeInternal() {
    Log.d(TAG, "Closing peer connection.");
    statsTimer.cancel();
    if (api != null) {
      api.unpublishStream(localStreamId1);
      api.leaveStreamRoom(localStreamId2);
    }
    if (connection != null) {
      connection.disconnect();
    }
    Log.d(TAG, "Closing audio source.");
    if (audioSource != null) {
      audioSource.dispose();
      audioSource = null;
    }
    Log.d(TAG, "Stopping capture.");
    if (videoCapturer != null) {
      try {
        videoCapturer.stopCapture();
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      videoCapturerStopped = true;
      videoCapturer.dispose();
      videoCapturer = null;
    }
    Log.d(TAG, "Closing video source.");
    if (videoSource != null) {
      videoSource.dispose();
      videoSource = null;
    }
    if (surfaceTextureHelper != null) {
      surfaceTextureHelper.dispose();
      surfaceTextureHelper = null;
    }
    if (saveRecordedAudioToFile != null) {
      Log.d(TAG, "Closing audio file for recorded input audio.");
      saveRecordedAudioToFile.stop();
      saveRecordedAudioToFile = null;
    }
    localRender = null;
    Log.d(TAG, "Closing peer connection factory.");
    if (factory != null) {
      factory.dispose();
      factory = null;
    }
    rootEglBase.release();
    Log.d(TAG, "Closing peer connection done.");
    PeerConnectionFactory.stopInternalTracingCapture();
    PeerConnectionFactory.shutdownInternalTracer();
  }

  private void reportError(final String errorMessage) {
    Log.e(TAG, "Peerconnection error: " + errorMessage);
    executor.execute(() -> {
      if (!isError) {
        isError = true;
      }
    });
  }

  @Nullable
  private AudioTrack createAudioTrack() {
    audioSource = factory.createAudioSource(audioConstraints);
    localAudioTrack = factory.createAudioTrack(AUDIO_TRACK_ID, audioSource);
    localAudioTrack.setEnabled(enableAudio);
    return localAudioTrack;
  }

  @Nullable
  private VideoTrack createVideoTrack(VideoCapturer capturer) {
    surfaceTextureHelper =
        SurfaceTextureHelper.create("CaptureThread", rootEglBase.getEglBaseContext());
    videoSource = factory.createVideoSource(false);
    capturer.initialize(surfaceTextureHelper, appContext, videoSource.getCapturerObserver());
    capturer.startCapture(1920, 1080, 30);

    localVideoTrack = factory.createVideoTrack(VIDEO_TRACK_ID, videoSource);
    localVideoTrack.setEnabled(renderVideo);
    localVideoTrack.addSink(localRender);
    return localVideoTrack;
  }
}
*/
